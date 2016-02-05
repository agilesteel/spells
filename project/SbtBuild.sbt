resolvers ++= Seq(
  Resolver.url("scoverage-bintray", url("https://dl.bintray.com/sksamuel/sbt-plugins/"))(Resolver.ivyStylePatterns),
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
)

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.2.1")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.9")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.1")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.5.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("org.scoverage" %% "sbt-coveralls" % "1.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

libraryDependencies += "com.github.agilesteel" %% "spells" % "1.6.1"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  // "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)
