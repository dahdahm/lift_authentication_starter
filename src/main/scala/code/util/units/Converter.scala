package code.util.units

import math._
import net.liftweb.common.Box

/**
 * Utility class to convert strings to other types and to Box them.
 * Example:
 * 	Converter.parse[Double]("112345"))
 *  returns a boxed double 
 * Converter.parse[Double]("asdf"))
 * returns None.
 */
object Converter {

  case class ParseOp[T](op: String => T)
  implicit val popDouble = ParseOp[Double](_.toDouble)
  implicit val popInt = ParseOp[Int](_.toInt)

  /**
   * parses a string and boxes it into ParseOp type.
   */
  def parse[T: ParseOp](s: String) =
    try {
      Some(implicitly[ParseOp[T]].op(s))
    } catch { case _ => None }

   /**
   * parses a boxed string and boxes it into ParseOp type.
   */
  def parse[T: ParseOp](s: Box[String]) =
    try {
      Some(implicitly[ParseOp[T]].op(s openOr ""))
    } catch { case _ => None }

}