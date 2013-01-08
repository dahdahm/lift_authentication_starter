package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._
import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper._
import net.liftweb.sitemap.Loc._
import code.model._
import net.liftmodules.JQueryModule
import net.liftweb.widgets.tablesorter.TableSorter
import net.liftweb.widgets.menu.MenuWidget
import java.util.Locale
import net.liftweb.http.provider.HTTPRequest
import omniauth.Omniauth;
import omniauth.lib._

object localeOverride extends SessionVar[Box[Locale]](Empty)

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Logger {
  actor.ThreadPoolRules.nullContextClassLoader = true

  val dbDriver = Props.get("db.driver").get
  val dbUrl = Props.get("db.url").get
  val dbUser = Props.get("db.user")
  val dbPassword = Props.get("db.password")

  /**
   * Returns DB vendor depending on whether this app runs in production or
   * test/dev environment. This will be defined by JVM parameter: -Drun.mode=production
   * and depends on props files available.
   * This is a fallback method which should be called if your servlet container
   * does not provide database connectivity via JNDI.
   * See more info at: https://www.assembla.com/spaces/liftweb/wiki/Run_Modes
   * We expect an in-memory DB to be present for dev and test modes, and real DB for
   * production and similar environments.
   */
  def getDBVendor: StandardDBVendor =
    new StandardDBVendor(dbDriver, dbUrl, dbUser, dbPassword)

  def boot {
    info("Running in [" + Props.mode + "] mode.")
    // ??? TODO: define JNDI in tomcat: DefaultConnectionIdentifier.jndiName = "whatever" or keep it to lift
    if (DB.jndiJdbcConnAvailable_?) {
      info("DB connectivity is available in servlet container via JNDI.")
    } else {
      info("DB connectivity is not available in servlet container via JNDI. Selecting alternative DB connectivity...")
      val vendor = getDBVendor
      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
      info("DB Connection details: driver [" + dbDriver + "], URL [" + dbUrl +
        "], username [" + dbUser.getOrElse("") + "].")
    }

    // where to search snippet
    LiftRules.addToPackages("code")
    Schemifier.schemify(true, Schemifier.infoF _, User)

    // Logged in test, used to enable/disable pages from logged-in/logged-out users. 
    //TODO: Enable after fixign authentication through facebook
    //val loggedIn = If(() => User.loggedIn_?, () => RedirectResponse("/user_mgt/login"))
     val loggedIn = If(() => true, () => RedirectResponse("/user_mgt/login"))

    val authmenus = Menu("User") / "index" >> LocGroup("public") >> loggedIn >> PlaceHolder submenus (
      User.menus)

    def sitemap() = SiteMap((
      List(
        authmenus, // Simple menu form
        // Menu with special Link
        Menu(Loc("Static", Link(List("static"), true, "/static/index"),
          "Static Content"))) ::: Omniauth.sitemap): _*)

    LiftRules.setSiteMap(sitemap);
    
       Omniauth.initWithProviders(List(new FacebookProvider("YOUR_FACEBOOK_KEY", "YOUR_FACEBOOK_SECRET")))
// Set this in your facebook app.
    Omniauth.siteAuthBaseUrl="""http://localhost:8080/"""

    // TODO Move this to an external file.
    //Localization block
    /**
     * This code sets the localeCalculator method which lift uses to determine which locale it needs.
     * default locale is french, and everytime it encounters a locale with the param "hl" in the url,
     *  it will set it throughout the session and use it.
     */
    val frenchLocale = new Locale("fr", "FR")
    object localeMemo extends RequestMemoize[Int, Locale] {
      override protected def __nameSalt = randomString(20)
    }

    LiftRules.localeCalculator = (request: Box[HTTPRequest]) =>
      localeMemo(request.hashCode, (for {
        r <- request
        p <- tryo(r.param("hl").head.split(Array('_', '-')))
      } yield p match {
        case Array(lang) => {
          if (!lang.equals("hl")) {
            debug("setting local to " + lang)
            localeOverride.set(Full(new Locale(lang)))
          }
          localeOverride.openOrThrowException("If this fails, then the locale is not defined!")
        }
        case Array(lang, country) => {
          debug("setting local to " + lang + "_" + country)
          localeOverride.set(Full(new Locale(lang, country)))
          localeOverride.openOrThrowException("If this fails, then the locale is not defined!")
        }
      }).openOr(localeOverride.openOr(frenchLocale)))
    //end localization block

    // This transforms urls from users/delete/3 to users/delete?id=3 to be handled by the DeleteUsers.scala snippet.
    LiftRules.statelessRewrite.append {
      case RewriteRequest(
        ParsePath(List("users", "delete", id), _, _, _), _, _) => //TODO: replace "delete" with action - does not work yet
        RewriteResponse("users" :: "delete" :: Nil, Map("id" -> id))
    }

    // Initializes the jquery lift widget
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery = JQueryModule.JQuery172
    JQueryModule.init()
    MenuWidget.init()

    TableSorter.init

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    LiftRules.noticesAutoFadeOut.default.set((noticeType: NoticeType.Value) => Full((1 seconds, 2 seconds)))

    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    LiftRules.dispatch.append(DefaultOpenIDVendor.dispatchPF)
    LiftRules.snippets.append(DefaultOpenIDVendor.snippetPF)

    S.addAround(DB.buildLoanWrapper)
  }
}
