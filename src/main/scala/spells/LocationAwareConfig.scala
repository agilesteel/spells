package spells

import java.io.File

import com.typesafe.config._
import ConfigFactory._

private[spells] trait LocationAwareConfig {
  private val config: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private def userConfig = new File(userConfigLocation)
  private def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""

  config.checkValid(defaultReference(), "spells")

  private[spells] implicit def porpertyToBoolean(property: LocationAwareProperty[Boolean]): Boolean =
    config getBoolean property.location

  private[spells] implicit def porpertyToDouble(property: LocationAwareProperty[Double]): Double =
    config getDouble property.location

  private[spells] implicit def porpertyToInt(property: LocationAwareProperty[Int]): Int =
    config getInt property.location

  private[spells] implicit def porpertyToLong(property: LocationAwareProperty[Long]): Long =
    config getLong property.location

  private[spells] implicit def porpertyToString(property: LocationAwareProperty[String]): String =
    config getString property.location
}
