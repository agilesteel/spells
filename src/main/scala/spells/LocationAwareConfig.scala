package spells

import java.io.File

import com.typesafe.config._
import ConfigFactory._

private[spells] trait LocationAwareConfig {
  private val config: Config =
    if (userConfig.exists)
      load(parseFile(userConfig)).withFallback(load)
    else load

  private def userConfigLocation = s"""${System.getProperty("user.home")}/.spells.conf"""
  private def userConfig = new File(userConfigLocation)

  config.checkValid(defaultReference(), "spells")

  implicit def porpertyToBoolean(property: LocationAwareProperty) = config getBoolean property.location
  implicit def porpertyToDouble(property: LocationAwareProperty) = config getDouble property.location
  implicit def porpertyToInt(property: LocationAwareProperty) = config getInt property.location
  implicit def porpertyToLong(property: LocationAwareProperty) = config getLong property.location
  implicit def porpertyToString(property: LocationAwareProperty) = config getString property.location
}
