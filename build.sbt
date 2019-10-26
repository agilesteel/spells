lazy val spells = (project in file("."))
  .settings(
    name := "spells",
    organization := "com.github.agilesteel",
    version := "2.2.0",
    scalaVersion := "2.12.10",
    crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.10"),
    homepage := Some(url("https://agilesteel.github.io/spells/")),
    startYear := some(2012),
    description := """This is a small scala "util" library, which will hopefully grow over time.""",
    licenses := Seq(
      "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    )
  )
  .settings(testAndLayoutSettings: _*)
  .settings(spellsSettings: _*)
  .settings(pureScalaProjectSettings: _*)
  .settings(publishSettings: _*)
  .settings(aliasSettings: _*)
  .enablePlugins(BuildInfoPlugin)
  .settings(buildInfoSettings: _*)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](scalaVersion),
  buildInfoPackage := "spells",
  buildInfoObject := "SpellsBuildInfo"
)

lazy val spellsSettings = Seq(
  initialCommands in console :=
  s"""|object user extends spells.Spells
      |import user._
      |import scala.concurrent.duration._""".stripMargin,
  libraryDependencies ++= Dependencies(scalaVersion.value.toString),
  onLoad in Global := {
    val checkForDepdendencyUpdates =
      (state: State) => "dependencyUpdates" :: state
    checkForDepdendencyUpdates compose (onLoad in Global).value
  },
  scalacOptions in (Compile, console) := nonDestructiveCompilerFlags,
  scalacOptions ++= nonDestructiveCompilerFlags ++ {
    if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion.value)))
      Seq.empty[String]
    else
      Seq(
        "-Xlint:-unused,_",
        "-Ywarn-dead-code",
        "-Ywarn-unused-import"
      )
  }
)

lazy val nonDestructiveCompilerFlags = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked"
)

lazy val testAndLayoutSettings = Seq(
  testOptions in Test += Tests.Argument(
    TestFrameworks.ScalaTest,
    "-oSD",
    "-h",
    "target/scalatest-reports"
  ),
  testOptions in Test += Tests.Setup(
    UserConfigFileManager.createSpellsConfigFileForCurrentUser
  ),
  testOptions in Test += Tests.Cleanup(
    UserConfigFileManager.deleteSpellsConfigFileForCurrentUser
  )
)

lazy val pureScalaProjectSettings = Seq(
  unmanagedSourceDirectories in Compile := ((scalaSource in Compile)(Seq(_))).value,
  unmanagedSourceDirectories in Test := ((scalaSource in Test)(Seq(_))).value
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials"),
  publishTo := {
    (version) {
      version: String =>
        if (version.trim.endsWith("SNAPSHOT"))
          Some(
            "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
          )
        else
          Some(
            "Sonatype Nexus Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
          )
    }
  }.value,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  pomExtra := {
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
    </developers>
  }
)

lazy val aliasSettings =
  addCommandAlias("testWithCoverage", "; clean; coverage; test; coverageReport") ++
    addCommandAlias("deploySnapshot", "; +clean; +test; +publish") ++
    addCommandAlias(
      "deploy",
      "; +clean; +test; +publishSigned; sonatypeReleaseAll"
    ) ++
    addCommandAlias(
      "pluginUpdates",
      "; reload plugins; dependencyUpdates; reload return"
    )

def SemVer(scalaVersion: String): (Int, Int, Int) = {
  val version = scalaVersion.split("\\.")

  val major = version.head.toInt
  val minor = version.tail.head.toInt
  val patch = version.tail.tail.head.toInt

  (major, minor, patch)
}

def `isScalaVersionSmallerThan 2.12`(semVer: (Int, Int, Int)): Boolean = {
  val (major, minor, patch) = semVer

  major == 2 && minor < 12
}

def Dependencies(scalaVersion: String) = {
  val `scala-reflect` = "org.scala-lang" % "scala-reflect" % scalaVersion

  val config = {
    val configVersion: String =
      if (`isScalaVersionSmallerThan 2.12`(SemVer(scalaVersion))) "1.2.1"
      else "1.3.3"

    "com.typesafe" % "config" % configVersion
  }

  val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

  Seq(`scala-reflect`, config, pegdown, scalaTest)
}

lazy val UserConfigFileManager = new {
  import java.nio.file.{Files, Paths}
  import java.nio.file.StandardCopyOption._
  import java.nio.file.LinkOption._

  val userConfigLocationValue =
    s"""${System.getProperty("user.home")}/.spells.conf"""
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
      Files.move(
        userConfigLocation,
        userConfigBackupLocation,
        ATOMIC_MOVE,
        REPLACE_EXISTING,
        NOFOLLOW_LINKS
      )
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
      Files.move(
        userConfigBackupLocation,
        userConfigLocation,
        ATOMIC_MOVE,
        REPLACE_EXISTING,
        NOFOLLOW_LINKS
      )
  }
}
