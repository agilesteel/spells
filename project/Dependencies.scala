import sbt._

import Util._

object Dependencies extends (String => Seq[ModuleID]) {
  def apply(scalaVersion: String): Seq[ModuleID] = {
    val `scala-reflect` = "org.scala-lang" % "scala-reflect" % scalaVersion

    val `scala-collection-compat` =
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.6"

    val config = {
      val configVersion: String =
        if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion))) "1.2.1"
        else "1.4.0"

      "com.typesafe" % "config" % configVersion
    }

    object Test {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.2.0"
    }

    Seq(
      `scala-collection-compat`,
      `scala-reflect`,
      config,
      Test.scalatest % sbt.Test
    )
  }
}
