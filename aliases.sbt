import Util._

addCommandAlias("cd", "project")
addCommandAlias("root", "cd spells")
addCommandAlias("c", "compile")
addCommandAlias("ca", "test:compile")
addCommandAlias("t", "test")
addCommandAlias("r", "run")
addCommandAlias(
  "up2date",
  "reload plugins; dependencyUpdates; reload return; dependencyUpdates"
)

addCommandAlias("testWithCoverage", "clean; coverage; test; coverageReport")
addCommandAlias("deploySnapshot", "clean; test; publish")
addCommandAlias("deploy", "clean; test; publishSigned; sonatypeReleaseAll")
addCommandAlias("deploySnapshotAll", "+clean; +test; +publish")
addCommandAlias(
  "deployAll",
  "+clean; +test; +publishSigned; sonatypeReleaseAll"
)

onLoadMessage +=
  s"""|
      |────────────────────────────────────
      |      List of defined ${styled("aliases")}
      |─────────────────┬──────────────────
      |${styled("cd")}               │ project
      |${styled("root")}             │ cd spells
      |${styled("c")}                │ compile
      |${styled("ca")}               │ compile all
      |${styled("t")}                │ test
      |${styled("r")}                │ run
      |${styled("up2date")}          │ dependencyUpdates
      |${styled("deploy")}           │ deploy
      |${styled("deploySnapshot")}   │ deploySnapshot
      |${styled("deployAll")}        │ deployAll
      |${styled("deploySnapshotAll")}│ deploySnapshotAll
      |${styled("testWithCoverage")} │ testWithCoverage
      |─────────────────┴──────────────────""".stripMargin
