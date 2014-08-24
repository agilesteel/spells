addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.2.1")

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.4.4"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_")
