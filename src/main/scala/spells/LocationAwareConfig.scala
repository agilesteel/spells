package spells

import java.io.File

import com.typesafe.config._
import ConfigFactory._

private[spells] trait LocationAwareConfig {
  private[spells] implicit lazy val config: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private def userConfig = new File(userConfigLocation)
  private def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""

  config.checkValid(defaultReference(), "spells")

  private[spells] implicit def propertyToBoolean(property: LocationAwareProperty[Boolean]): Boolean =
    config getBoolean property.location

  private[spells] implicit def propertyToInt(property: LocationAwareProperty[Int]): Int =
    config getInt property.location

  // Commented out, because it's not used yet... for better coverage ;)

  // private[spells] implicit def propertyToDouble(property: LocationAwareProperty[Double]): Double =
  //   config getDouble property.location

  // private[spells] implicit def propertyToLong(property: LocationAwareProperty[Long]): Long =
  //   config getLong property.location

  // private[spells] implicit def propertyToString(property: LocationAwareProperty[String]): String =
  //   config getString property.location
}
