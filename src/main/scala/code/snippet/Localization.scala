package code
package snippet

import code._
import model._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import scala.xml._

class L{

  def languageSelect = "#languages" #> (if (S.locale.toString() == "fr_FR") { <li><a href="?hl=en_EN">English</a></li> } else <li><a href="?hl=fr_FR">Français</a></li>)
}
