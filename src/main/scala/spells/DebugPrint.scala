package spells

object DebugPrint {
  private[spells] trait Deps {
    def isCopyFilePathToClipboardWhenDebugPrintingFeatureEnabled: Boolean =
      spells.feature.`copy-file-path-to-clipboard-when-debug-printing`

    def clipboardWriter: String => Unit = Clipboard.writeString
  }
}

trait DebugPrint extends DebugPrint.Deps {
  final def debugPrintln(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0): Unit = {
    Console println stackTraced(style, input, increaseStackTraceDepthBy)
  }

  final def debugPrint(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0): Unit = {
    Console print stackTraced(style, input, increaseStackTraceDepthBy)
  }

  final def stackTraced(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0): String =
    stackTraced(style, input, increaseStackTraceDepthBy)

  private def stackTraced(style: Ansi#AnsiStyle, input: Any, increaseStackTraceDepthBy: Int): String = {
    val element = currentLineStackTraceElement(increaseStackTraceDepthBy)
    writeToClipboard(renderLocation(element))
    styled(s"$input at $element")(style)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  private def renderLocation(element: StackTraceElement): String =
    element.toString
      .replace(s"${element.getClassName}.${element.getMethodName}", "")
      .stripPrefix("(").stripSuffix(")")

  final def writeToClipboard: String => Unit =
    if (isCopyFilePathToClipboardWhenDebugPrintingFeatureEnabled)
      clipboardWriter
    else
      _ => {}
}
