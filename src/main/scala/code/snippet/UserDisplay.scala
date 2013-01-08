package code
package snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.model._
import Helpers._

class UserDisplay {
  def summary (xhtml : NodeSeq) : NodeSeq = User.currentUser match {
    case Full(user) => {
	<p>Hello <b>{user.niceName}</b>!</p>
    }
    case _ => <div>  </div>
  }
}


