package spells

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory._

private[spells] trait LocationAwareConfig {
  private[spells] final implicit lazy val config: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private final def userConfig = new java.io.File(userConfigLocation)
  private final def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""

  config.checkValid(defaultReference(), "spells")

  final implicit def locationAwarePropertyToBoolean(property: LocationAwareProperty[Boolean]): Boolean =
    config getBoolean property.location

  final implicit def locationAwarePropertyToInt(property: LocationAwareProperty[Int]): Int =
    config getInt property.location

  // Commented out, because it's not used yet... for better coverage ;)

  // final implicit def locationAwarePropertyToDouble(property: LocationAwareProperty[Double]): Double =
  //   config getDouble property.location

  // final implicit def locationAwarePropertyToLong(property: LocationAwareProperty[Long]): Long =
  //   config getLong property.location

  // final implicit def locationAwarePropertyToString(property: LocationAwareProperty[String]): String =
  //   config getString property.location
}
