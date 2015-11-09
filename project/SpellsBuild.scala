package spells

import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform._
import com.typesafe.sbt.SbtScalariform.ScalariformKeys._

object SpellsBuild extends Build {
  lazy val projectName = "spells"

  lazy val buildSettings = Seq(
    name := projectName,
    organization := "com.github.agilesteel",
    version := "2.0.0-SNAPSHOT",
    scalaVersion := "2.11.7",
    crossScalaVersions := Seq("2.10.6", "2.11.7"),
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
    .configs(Build)
    .settings(testAndLayoutSettings: _*)
    // The following line has to come after testAndLayoutSettings
    .settings(inConfig(Build)(configScalariformSettings): _*)

  lazy val spellsSettings = Seq(
    incOptions := incOptions.value.withNameHashing(true),
    initialCommands in console := "object user extends spells.SpellsModule;import user._;import scala.concurrent.duration._",
    libraryDependencies ++= Dependencies.all,
    onLoad in Global := {
      val checkForDepdendencyUpdates = (state: State) => "dependencyUpdates" :: state
      checkForDepdendencyUpdates compose (onLoad in Global).value
    },
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-language:_",
      "-unchecked",
      "-Xlint",
      "-Ywarn-adapted-args",
      "-Ywarn-inaccessible"
      // "-Ywarn-value-discard"
    ) ++ {
      if(scalaVersion.value.startsWith("2.11"))
        Seq("-Ywarn-dead-code", "-Ywarn-unused-import")
      else
        Seq.empty[String]
    }
  )

  lazy val Build = config("build") extend Compile

  lazy val testAndLayoutSettings = Seq(
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oSD", "-h", "target/scalatest-reports"),
    testOptions in Test += Tests.Setup(UserConfigFileManager.createSpellsConfigFileForCurrentUser),
    testOptions in Test += Tests.Cleanup(UserConfigFileManager.deleteSpellsConfigFileForCurrentUser),
    scalaSource in Build := baseDirectory.value / "project",

    compileInputs in (Test, compile) <<= (compileInputs in (Test, compile)) dependsOn (format in Test),
    // This one doesn't work yet, but we can already do 'build:scalariformFormat' manually
    compileInputs in (Build, compile) <<= (compileInputs in (Build, compile)) dependsOn (ScalariformKeys.format in Build)
  )

  lazy val pureScalaProjectSettings = Seq(
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))
  )

  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials"),
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
    addCommandAlias("man", "test:run") ++
    addCommandAlias("e", "test:run-main") ++ {
      val info =
        s"""|Type ${"man".magenta} to see the list of examples.
            |Type ${"e".magenta} ${"name".cyan} to run a specific example (keep in mind that autocompletion requires prior compilation).""".stripMargin
      Seq(
        onLoadMessage <<= onLoadMessage { msg => msg + '\n' + info }
      )
    }
}

object Dependencies {
  val config = "com.typesafe" % "config" % "1.3.0"

  val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "2.2.5" % "test"

  val all = Seq(config, pegdown, scalaTest)
}

object UserConfigFileManager {
  import java.nio.file.{ Files, Paths }
  import java.nio.file.StandardCopyOption._
  import java.nio.file.LinkOption._

  val userConfigLocationValue = s"""${System.getProperty("user.home")}/.spells.conf"""
  val userConfigLocation = Paths get userConfigLocationValue
  val userConfigBackupLocation = Paths get (userConfigLocationValue + "_backup")

  val createSpellsConfigFileForCurrentUser: () => Unit = () => {
    backup()
    create()
  }

  val deleteSpellsConfigFileForCurrentUser: () => Unit = () => {
    delete()
    restore()
  }

  def backup(): Unit = {
    if (Files exists userConfigLocation)
      Files.move(userConfigLocation, userConfigBackupLocation, ATOMIC_MOVE, REPLACE_EXISTING, NOFOLLOW_LINKS)
  }

  def create(): Unit = {
    val userConfigContent =
      s"""|spells {
          |  coverage {
          |    should-be-happy = yes
          |  }
          |}""".stripMargin

    Files.write(userConfigLocation, userConfigContent.getBytes)
  }

  def delete(): Unit = {
    Files.delete(userConfigLocation)
  }

  def restore(): Unit = {
    if (Files exists userConfigBackupLocation)
      Files.move(userConfigBackupLocation, userConfigLocation, ATOMIC_MOVE, REPLACE_EXISTING, NOFOLLOW_LINKS)
  }
}
