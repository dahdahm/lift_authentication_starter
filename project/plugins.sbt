libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1")

//SBT idea plugin for generating IntelliJ Idea project.
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.1.0")

//SBT eclipse plugin for generating Eclipse project. Simply run 'eclipse' in SBT cmd.
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")
