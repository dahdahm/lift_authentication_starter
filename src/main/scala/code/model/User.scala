package code
package model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

import net.liftweb.openid.{ OpenIDProtoUser, MetaOpenIDProtoUser }

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaOpenIDProtoUser[User] with LongKeyedMetaMapper[User] with Logger {
  def openIDVendor = DefaultOpenIDVendor
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default_wide" at="content"><lift:bind/></lift:surround>)

  override def loginXhtml = <lift:embed what="login"/>
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
    locale, timezone, password, textArea)

  // comment this line out to require email validations
  override def skipEmailValidation = true
}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User extends LongKeyedMapper[User] with OpenIDProtoUser[User] {
  def getSingleton = User // what's the "meta" server

  // define an additional field for a personal essay
  object textArea extends MappedTextarea(this, 2048) {
    override def textareaRows = 10
    override def textareaCols = 50
    override def displayName = "Personal Essay"
  }

  //Default OpenIDProtoUser implementation uses nickname so swapping this out for standard FirstName Surname (Email) format.
  override def niceName: String = (firstName.is, lastName.is, email.is) match {
    case (f, l, e) if f.length > 1 && l.length > 1 => f + " " + l + " (" + e + ")"
    case (f, _, e) if f.length > 1 => f + " (" + e + ")"
    case (_, l, e) if l.length > 1 => l + " (" + e + ")"
    case (_, _, e) => e
  }

}

