package spells

private[spells] trait LocationAwareConfigModule {
  this: AnsiModule =>

  import com.typesafe.config.Config
  import com.typesafe.config.ConfigFactory._

  spellsConfig.checkValid(defaultReference(), "spells")

  implicit final lazy val spellsConfig: Config = loadSpellsConfig

  def loadSpellsConfig: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private final def userConfig = new java.io.File(userConfigLocation)
  private final def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""

  final implicit def locationAwarePropertyToBoolean(property: LocationAwareProperty[Boolean]): Boolean =
    spellsConfig getBoolean property.location

  final implicit def locationAwarePropertyToInt(property: LocationAwareProperty[Int]): Int =
    spellsConfig getInt property.location

  // Unresolved compiler bug: https://issues.scala-lang.org/browse/SI-5643
  final implicit def locationAwarePropertyToAnsiStyle(property: LocationAwareProperty[AnsiModule#AnsiStyle]): AnsiModule#AnsiStyle =
    spellsConfig getString property.location toAnsiStyle

  // Commented out, because it's not used yet... for better coverage ;)

  // final implicit def locationAwarePropertyToDouble(property: LocationAwareProperty[Double]): Double =
  //   spellsConfig getDouble property.location

  // final implicit def locationAwarePropertyToLong(property: LocationAwareProperty[Long]): Long =
  //   spellsConfig getLong property.location

  // final implicit def locationAwarePropertyToString(property: LocationAwareProperty[String]): String =
  //   spellsConfig getString property.location
}