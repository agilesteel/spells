resolvers ++= Seq(
  Resolver.url("scoverage-bintray", url("https://dl.bintray.com/sksamuel/sbt-plugins/"))(Resolver.ivyStylePatterns),
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
)

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.12")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.2.4")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

libraryDependencies += "com.github.agilesteel" %% "spells" % "2.1.0"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked"
)
