package spells

import scala.util.control.Exception._
import scala.util.Try

trait ClipboardModule {
  this: Misc =>

  trait Clipboard {
    def writeString(content: String): Unit
    def readString: Try[String]
  }

  object Clipboard extends Clipboard {
    import java.awt.datatransfer.{ StringSelection, DataFlavor }
    import java.awt.Toolkit

    private val awtLoggers =
      Vector(
        "java.awt",
        "java.awt.event",
        "java.awt.focus",
        "java.awt.mixing",
        "sun.awt",
        "sun.awt.windows",
        "sun.awt.X11"
      )

    awtLoggers foreach disableLogging

    private def disableLogging(loggerName: String): Unit = {
      java.util.logging.Logger getLogger "" setLevel java.util.logging.Level.OFF
    }

    private lazy val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard

    def writeString(content: String): Unit = ignoring(classOf[Throwable]) {
      val contentAsStringSelection = new StringSelection(content)
      clipboard setContents (contentAsStringSelection, contentAsStringSelection)
    }

    def readString: Try[String] = Try {
      clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor).asInstanceOf[String]
    }
  }
}
