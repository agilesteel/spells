publishMavenStyle := true

credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials")

Test / publishArtifact := false

pomIncludeRepository := { _ =>
  false
}

publishTo := {
  (version) { version: String =>
    if (version.trim.endsWith("SNAPSHOT"))
      Some(
        "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
      )
    else
      Some(
        "Sonatype Nexus Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
      )
  }
}.value

scmInfo := Some(
  ScmInfo(
    url("https://github.com/agilesteel/spells"),
    "scm:git:git@github.com:agilesteel/spells.git"
  )
)

developers := List(
  Developer(
    id = "agilesteel",
    name = "Vladyslav Pekker",
    email = "agilesteel@gmail.com",
    url = url("http://about.me/agilesteel")
  )
)
