package spells

import sbt._
import Keys._

object SpellsBuild extends Build {
  lazy val buildSettings = Seq(
    organization := "com.github.agilesteel",
    version := "1.1",
    scalaVersion := "2.9.2",
    homepage := Some(url("http://agilesteel.github.com/spells/")),
    startYear := some(2012),
    description := """This is a small scala "util" library, which will hopefully grow over time.""",
    licenses += "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

  override lazy val settings = super.settings ++ buildSettings

  lazy val root = Project(
    id = "spells",
    base = file("."),
    settings = Project.defaultSettings ++ spellsSettings ++ pureScalaProjectSettings)

  lazy val spellsSettings = Seq(
    libraryDependencies += Dependency.scalaTest,

    scalacOptions ++= Seq("-unchecked", "-deprecation"),

    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "stdout"))

  lazy val pureScalaProjectSettings = Seq(
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_)))
}

object Dependency {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "latest.release" % "test"
}