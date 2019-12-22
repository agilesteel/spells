import Util._

lazy val spells =
  project
    .in(file("."))
    .settings(
      name := "spells",
      organization := "com.github.agilesteel",
      version := "2.2.1",
      scalaVersion := "2.13.1",
      homepage := Some(url("https://agilesteel.github.io/spells/")),
      startYear := some(2012),
      description := """This is a small scala "util" library, which will hopefully grow over time.""",
      licenses := Seq(
        "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")
      ),
      initialCommands in console :=
        s"""|object user extends spells.Spells
            |import user._
            |import scala.concurrent.duration._""".stripMargin,
      libraryDependencies ++= Dependencies(scalaVersion.value.toString),
      Compile / scalacOptions ++= {
        Seq(
          "-deprecation",
          "-feature",
          "-language:_",
          "-unchecked"
        ) ++ {
          if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion.value)))
            Seq.empty
          else
            Seq(
              "-Ywarn-unused:_",
              "-Xfatal-warnings"
            )
        }
      },
      Test / scalacOptions --= Seq(
        "-Ywarn-unused:_",
        "-Xfatal-warnings"
      ),
      Compile / console / scalacOptions --= Seq(
        "-Ywarn-unused:_",
        "-Xfatal-warnings"
      ),
      Test / console / scalacOptions :=
        (Compile / console / scalacOptions).value
    )
