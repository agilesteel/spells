package spells

package object user extends spells.Spells {
  // Turns java.lang.String into String for Scala version > 2.12
  private[user] def typeBasedOnScalaVersion(input: String): String =
    if (`isScalaVersionSmallerThan 2.12`)
      input
    else
      input.substring(input.lastIndexOf(".") + 1)

  private def `isScalaVersionSmallerThan 2.12`: Boolean =
    SemVer.major == 2 && SemVer.minor < 12

  private object SemVer {
    private val version = SpellsBuildInfo.scalaVersion.split("\\.")

    val major = version.head.toInt
    val minor = version.tail.head.toInt
    val patch = version.tail.tail.head.toInt
  }
}
