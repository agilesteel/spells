package spells

private[spells] trait LocationAwareConfigModule {
  this: AnsiModule =>

  import com.typesafe.config.Config
  import com.typesafe.config.ConfigFactory._

  spellsConfig.checkValid(defaultReference(), "spells")

  final lazy val spellsConfig: Config = loadSpellsConfig

  protected def loadSpellsConfig: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private final def userConfig = new java.io.File(userConfigLocation)
  private final def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""

  implicit final def locationAwarePropertyToBoolean(property: LocationAwarePropertyModule#LocationAwareProperty[Boolean]): Boolean =
    locationAwarePropertyTo(property, spellsConfig getBoolean property.location)

  implicit final def locationAwarePropertyToInt(property: LocationAwarePropertyModule#LocationAwareProperty[Int]): Int =
    locationAwarePropertyTo(property, spellsConfig getInt property.location)

  // Unresolved compiler bug: https://issues.scala-lang.org/browse/SI-5643
  implicit final def locationAwarePropertyToAnsiStyle(property: LocationAwarePropertyModule#LocationAwareProperty[AnsiModule#AnsiStyle]): AnsiModule#AnsiStyle =
    locationAwarePropertyTo(property, spellsConfig getString property.location toAnsiStyle)

  private[spells] def locationAwarePropertyTo[T](property: LocationAwarePropertyModule#LocationAwareProperty[T], value: T): T = {
    require(property isValid value, property validationErrorMessage value)

    value
  }

  // Commented out, because it's not used yet... for better coverage ;)

  // implicit final def locationAwarePropertyToDouble(property: AnsiModule#LocationAwareProperty[Double]): Double =
  //   spellsConfig getDouble property.location

  // implicit final def locationAwarePropertyToLong(property: AnsiModule#LocationAwareProperty[Long]): Long =
  //   spellsConfig getLong property.location

  // implicit final def locationAwarePropertyToString(property: AnsiModule#LocationAwareProperty[String]): String =
  //   spellsConfig getString property.location
}
