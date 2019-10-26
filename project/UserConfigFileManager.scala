import java.nio.file.{Files, Paths}
import java.nio.file.StandardCopyOption._
import java.nio.file.LinkOption._

object UserConfigFileManager {
  private[this] val userConfigLocationValue =
    s"""${System.getProperty("user.home")}/.spells.conf"""

  private[this] val userConfigLocation = Paths get userConfigLocationValue
  private[this] val userConfigBackupLocation = Paths get (userConfigLocationValue + "_backup")

  val createSpellsConfigFileForCurrentUser: () => Unit = () => {
    backup()
    create()
  }

  val deleteSpellsConfigFileForCurrentUser: () => Unit = () => {
    delete()
    restore()
  }

  private[this] def backup(): Unit = {
    if (Files exists userConfigLocation)
      Files.move(
        userConfigLocation,
        userConfigBackupLocation,
        ATOMIC_MOVE,
        REPLACE_EXISTING,
        NOFOLLOW_LINKS
      )
  }

  private[this] def create(): Unit = {
    val userConfigContent =
      s"""|spells {
          |  coverage {
          |    should-be-happy = yes
          |  }
          |}""".stripMargin

    Files.write(userConfigLocation, userConfigContent.getBytes)
  }

  private[this] def delete(): Unit = {
    Files.delete(userConfigLocation)
  }

  private[this] def restore(): Unit = {
    if (Files exists userConfigBackupLocation)
      Files.move(
        userConfigBackupLocation,
        userConfigLocation,
        ATOMIC_MOVE,
        REPLACE_EXISTING,
        NOFOLLOW_LINKS
      )
  }
}
