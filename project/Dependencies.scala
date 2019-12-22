import sbt._

import Util._

object Dependencies extends (String => Seq[ModuleID]) {
  def apply(scalaVersion: String): Seq[ModuleID] = {
    val `scala-reflect` = "org.scala-lang" % "scala-reflect" % scalaVersion

    val `scala-collection-compat` =
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"

    val config = {
      val configVersion: String =
        if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion))) "1.2.1"
        else "1.4.0"

      "com.typesafe" % "config" % configVersion
    }

    object Test {
      val pegdown =
        "org.pegdown" % "pegdown" % "1.6.0"

      val scalatest =
        "org.scalatest" %% "scalatest" % "3.0.8"
    }

    Seq(
      `scala-collection-compat`,
      `scala-reflect`,
      config,
      Test.pegdown % sbt.Test,
      Test.scalatest % sbt.Test
    )
  }
}
