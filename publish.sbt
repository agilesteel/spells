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

pomExtra := {
  <scm>
      <url>git@github.com:agilesteel/spells.git</url>
      <connection>scm:git:git@github.com:agilesteel/spells.git</connection>
    </scm>
    <developers>
      <developer>
        <id>agilesteel</id>
        <name>Vladyslav Pekker</name>
        <url>https://devinsideyou.com</url>
      </developer>
    </developers>
}
