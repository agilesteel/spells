package spells

private[spells] trait LocationAwareConfigModule {
  this: AnsiModule =>

  import scala.collection.JavaConverters._

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

  implicit final def locationAwarePropertyToString(property: LocationAwarePropertyModule#LocationAwareProperty[List[String]]): List[String] =
    locationAwarePropertyTo(property, spellsConfig.getStringList(property.location).asScala.toList)

  // Unresolved compiler bug: https://issues.scala-lang.org/browse/SI-5643
  implicit final def locationAwarePropertyToAnsiStyle(property: LocationAwarePropertyModule#LocationAwareProperty[AnsiModule#AnsiStyle]): AnsiModule#AnsiStyle =
    locationAwarePropertyTo(property, spellsConfig getString property.location toAnsiStyle)

  private[spells] def locationAwarePropertyTo[T](property: LocationAwarePropertyModule#LocationAwareProperty[T], value: T): T = {
    require(property isValid value, property validationErrorMessage value)

    value
  }
}
