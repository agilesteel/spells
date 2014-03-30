package spells

import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform._

object SpellsBuild extends Build {
  lazy val projectName = "spells"

  lazy val buildSettings = Project.defaultSettings ++ Seq(
    name := projectName,
    organization := "com.github.agilesteel",
    version := "1.6.0-SNAPSHOT",
    scalaVersion := "2.10.4",
    crossScalaVersions := Seq("2.10.0", "2.10.1", "2.10.2", "2.10.3", "2.10.4"),
    homepage := Some(url("http://agilesteel.github.com/spells")),
    startYear := some(2012),
    description := """This is a small scala "util" library, which will hopefully grow over time.""",
    licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")))

  lazy val root = Project(
    id = projectName,
    base = file("."),
    settings = buildSettings
            ++ spellsSettings
            ++ pureScalaProjectSettings
            ++ publishSettings
            ++ scalariformSettings
            ++ aliasSettings
  )

  lazy val spellsSettings = Seq(
    libraryDependencies ++= Dependencies.all,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-language:_"),
    initialCommands in console := "import spells._",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oSD", "-h", "target/reports")
  )

  lazy val pureScalaProjectSettings = Seq(
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))
  )

  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome/".sbt"/"sonatype.credentials"),
    publishTo <<= (version) { version: String =>
      if (version.trim.endsWith("SNAPSHOT"))
        Some("Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
      else
        Some("Sonatype Nexus Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra <<= (pomExtra, name, description) { (pom, name, desc) =>
      pom ++ xml.Group(
        <scm>
          <url>git@github.com:agilesteel/spells.git</url>
          <connection>scm:git:git@github.com:agilesteel/spells.git</connection>
        </scm>
        <developers>
          <developer>
            <id>agilesteel</id>
            <name>Vladyslav Pekker</name>
            <url>http://about.me/agilesteel</url>
          </developer>
        </developers>)
    }
  )

  lazy val aliasSettings =
    addCommandAlias("man",  "test:run") ++
    addCommandAlias("e",    "test:run-main") ++ {
      val info =  s"""|Type ${"man".magenta} to see the list of examples.
                      |Type ${"e".magenta} ${"name".cyan} to run a specific example (keep in mind that autocompletion requires prior compilation).""".stripMargin
      Seq(
        onLoadMessage <<= onLoadMessage { msg => msg + '\n' + info }
      )
    }
}

object Dependencies {
  val config = "com.typesafe" % "config" % "1.2.0"

  val pegdown = "org.pegdown" % "pegdown" % "1.4.2" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.1.0" % "test"

  val all = Seq(config, pegdown, scalaTest)
}
