package spells

object ClearPrint {
  final lazy val Clear = "\033[2K".toAnsiStyle
}

trait ClearPrint {
  @inline final def clearPrintln(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console println cleared(input)(style)
  }

  @inline final def clearPrint(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console print cleared(input)(style)
  }

  final def cleared(input: Any = "")(implicit style: AnsiStyle = Reset): String =
    styled(styled(s"\r$input")(ClearPrint.Clear))(style)
}
