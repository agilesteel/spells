package spells

object ClearPrint {
  final lazy val Clear = "\033[2K".toAnsiStyle
}

trait ClearPrint {
  final def clearPrintln(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console println cleared(input)(style)
  }

  final def clearPrint(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console print cleared(input)(style)
  }

  final def cleared(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): String =
    styled(styled(s"\r$input")(ClearPrint.Clear))(style)
}
