import sbt._
import Keys._

object SpellsBuild extends Build {
  lazy val root = Project(id = spells,
            		base = file("."),
            	    settings = spellsSettings)


  lazy val spells = "spells"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "latest.release"
  lazy val scalaz = "org.scalaz" %% "scalaz-core" % "latest.release"

  lazy val spellsSettings = Project.defaultSettings ++ Seq(
	name := spells,

	version := "1.0",

	libraryDependencies ++= Seq(
		scalaTest % "test",
		scalaz
	),

	scalacOptions ++= Seq(
		"-unchecked",
	 	"-deprecation"
	),

	testOptions in Test ++= Seq(
	  	Tests.Argument(TestFrameworks.ScalaTest, "-oS")
     	)
   )
}