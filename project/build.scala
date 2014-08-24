package spells

import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys._
import scoverage.ScoverageSbtPlugin._

object SpellsBuild extends Build {
  lazy val projectName = "spells"

  lazy val buildSettings = Seq(
    name := projectName,
    organization := "com.github.agilesteel",
    version := "1.6.0-SNAPSHOT",
    scalaVersion := "2.11.2",
    crossScalaVersions := Seq("2.10.0", "2.10.1", "2.10.2", "2.10.3", "2.10.4", "2.11.0", "2.11.1", "2.11.2"),
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
            ++ instrumentSettings
  )

  lazy val spellsSettings = Seq(
    compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (format in Test),
    incOptions := incOptions.value.withNameHashing(true),
    initialCommands in console := "import spells._",
    libraryDependencies ++= Dependencies.all,
    onLoad in Global := {
      val checkForDepdendencyUpdates = (state: State) => "dependencyUpdates" :: state
      checkForDepdendencyUpdates compose (onLoad in Global).value
    },
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-language:_"),
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
    addCommandAlias("pluginUpdates", "; reload plugins; dependencyUpdates; reload return") ++
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
  val config = "com.typesafe" % "config" % "1.2.1"

  val pegdown = "org.pegdown" % "pegdown" % "1.4.2" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.2" % "test"

  val all = Seq(config, pegdown, scalaTest)
}
