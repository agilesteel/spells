package spells

import sbt._
import Keys._

object Dependency {
  lazy val scalaTest = "org.scalatest" % "scalatest_2.10.0-RC5" % "latest.release" % "test"
  lazy val scalaActors = "org.scala-lang" % "scala-actors" % "latest.release" % "test"
}

object SpellsBuild extends Build {
  lazy val projectName = "spells"
  lazy val buildSettings = Seq(
    name := projectName,
    organization := "com.github.agilesteel",
    version := "1.2",
    scalaVersion := "2.10.0-RC5",
    homepage := Some(url("http://agilesteel.github.com/spells")),
    startYear := some(2012),
    description := """This is a small scala "util" library, which will hopefully grow over time.""",
    licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")))

  override lazy val settings = super.settings ++ buildSettings

  lazy val root = Project(
    id = projectName,
    base = file("."),
    settings = Project.defaultSettings ++ spellsSettings ++ pureScalaProjectSettings ++ publishSettings)

  lazy val spellsSettings = Seq(
    libraryDependencies ++= Seq(Dependency.scalaTest, Dependency.scalaActors),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:_"),
    initialCommands in console := "import spells._; import Spells._",
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "stdout"))

  lazy val pureScalaProjectSettings = Seq(
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_)))

  lazy val publishSettings = Seq(
    credentials += Credentials(Path.userHome/".sbt"/"sonatype.sbt"),
    publishTo <<= (version) { version: String =>
      if (version.trim.endsWith("SNAPSHOT"))
        Some("Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
      else
        Some("Sonatype Nexus Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    },
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
    })
}