package code
package snippet

import code._
import model._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import scala.xml._
import code.util.units.Bytes._
import math._
import net.liftweb.sitemap.Loc

import net.liftweb.sitemap.Loc._

object Breadcrumbs {

def breadcrumb = "*" #> {
	val breadcrumbs: List[Loc[_]] =
		for {
			currentLoc <- S.location.toList
			loc <- currentLoc.breadCrumbs
		} yield loc
	"li *" #> breadcrumbs.map{
		loc => ".link *" #> loc.title &
			".link [href]" #> loc.createDefaultLink.getOrElse(Text("#"))}}

}
