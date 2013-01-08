name := "liftauthstarter"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.9.1"

EclipseKeys.withSource := true

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases",
                "Omniauth repo" at "https://repository-liftmodules.forge.cloudbees.com/release"
                )
                
seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-M3"
  val widgetLiftVersion ="2.5-SNAPSHOT"
  Seq(
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    "junit" % "junit" % "4.10" % "test",
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-2.0"),
    "net.liftweb" %% "lift-ldap"   % liftVersion % "compile",
    "org.eclipse.jetty" % "jetty-plus" % "8.1.7.v20120910" % "container,test",
    "org.eclipse.jetty.orbit" % "javax.transaction" % "1.1.1.v201105210645" % "container,test"  artifacts Artifact("javax.transaction", "jar", "jar"),
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "org.eclipse.jetty.orbit" % "javax.mail.glassfish" % "1.4.1.v201005082020" % "container,test" artifacts Artifact("javax.mail.glassfish", "jar", "jar"),
    "org.eclipse.jetty.orbit" % "javax.activation" % "1.1.0.v201105071233" % "container,test" artifacts Artifact("javax.activation", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.12.1"           % "test",
    "com.h2database"    % "h2"                  % "1.3.167" % "container,test",
    "c3p0" % "c3p0" % "0.9.1.2" % "container,test",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "net.liftweb" %% "lift-widgets" % widgetLiftVersion % "compile->default",
    "org.liquibase" % "liquibase-core" % "2.0.5",
    "net.liftweb" %% "lift-openid" % widgetLiftVersion % "compile->default",
    "net.liftweb" %% "lift-wizard" % liftVersion % "compile->default",
    "net.liftmodules" %% "omniauth" % "2.4-0.6"
    )
}

libraryDependencies += "ch.qos.logback" % "logback-classic" % "0.9.26"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"