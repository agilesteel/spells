package spells

trait DebugPrint {
  final def debugPrintln(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console println stackTraced(style, input)
  }

  final def debugPrint(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console print stackTraced(style, input)
  }

  final def stackTraced(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): String =
    stackTraced(style, input)

  private def stackTraced(style: Ansi#AnsiStyle, input: Any): String =
    styled(s"$input at $currentLineStackTraceElement")(style)

  private def currentLineStackTraceElement = Thread.currentThread.getStackTrace apply 5
}
