enablePlugins(BuildInfoPlugin)

buildInfoKeys := Seq[BuildInfoKey](scalaVersion)
buildInfoPackage := "spells"
buildInfoObject := "SpellsBuildInfo"
