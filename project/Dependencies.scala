import sbt._

import Util._

object Dependencies extends (String => Seq[ModuleID]) {
  def apply(scalaVersion: String): Seq[ModuleID] = {
    val `scala-reflect` = "org.scala-lang" % "scala-reflect" % scalaVersion

    val config = {
      val configVersion: String =
        if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion))) "1.2.1"
        else "1.3.3"

      "com.typesafe" % "config" % configVersion
    }

    object Test {
      val pegdown =
        "org.pegdown" % "pegdown" % "1.6.0"

      val scalatest =
        "org.scalatest" %% "scalatest" % "3.0.5"
    }

    Seq(
      `scala-reflect`,
      config,
      Test.pegdown % sbt.Test,
      Test.scalatest % sbt.Test
    )
  }
}
