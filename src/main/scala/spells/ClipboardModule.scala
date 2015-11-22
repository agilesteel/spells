package spells

trait ClipboardModule {
  this: MiscModule =>

  import java.awt.datatransfer.{ DataFlavor, StringSelection }
  import java.awt.Toolkit
  import java.util.logging.Level
  import scala.util.Try

  trait Clipboard {
    def writeString(content: String): Try[Unit]
    def readString: Try[String]
  }

  object Clipboard extends Clipboard {
    Vector(
      "java.awt",
      "java.awt.event",
      "java.awt.focus",
      "java.awt.mixing",
      "sun.awt",
      "sun.awt.windows",
      "sun.awt.X11"
    ) foreach disableLogging

    private def disableLogging(loggerName: String): Unit = {
      java.util.logging.Logger getLogger "" setLevel Level.OFF
    }

    private lazy val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard

    override final def writeString(content: String): Try[Unit] = Try {
      val contentAsStringSelection = new StringSelection(content)
      clipboard setContents (contentAsStringSelection, contentAsStringSelection)
    }

    override final def readString: Try[String] = Try {
      clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
    }
  }
}
