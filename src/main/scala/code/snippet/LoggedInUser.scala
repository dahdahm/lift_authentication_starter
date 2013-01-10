package code.snippet;
import omniauth.Omniauth;
import omniauth._
import omniauth.lib._

import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb._
import http._
import SHtml._
import S._
import scala.xml._
import scala.math._
import net.liftweb.common.Empty

import java.io.File

import net.liftweb.common.Logger
import net.liftweb.http.S
import net.liftweb.http.LiftRules
import net.liftweb.common.{ Box, Full, Empty, Failure, ParamFailure }
import code.model.User

class LoggedInUser {

  def getUser(html: NodeSeq): NodeSeq = {

    Omniauth.currentAuth match {
      case Full(omni) => ({
        
    	  val user = User.create
    	  
    	  user.email(omni.email.get)
    	  user.save
    	  
    	  User.logUserIn(user)
    	  
    	  
    	  user.nickname(omni.name)
    	  user.save
        
        <div>
          provider:{ omni.provider }<br/>
          uid:{ omni.uid }<br/>
          name:{ omni.name }<br/>
          token:{ omni.token }<br/>
          nickname:{ omni.nickName }<br/>
          email:{ omni.email }<br/>
          firstName:{ omni.firstName }<br/>
          lastName:{ omni.lastName }<br/>
        </div>
      })
      case _ => <div> _</div>
    }

  }

}
