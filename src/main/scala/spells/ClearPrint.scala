package spells

trait ClearPrint {
  this: Ansi with StylePrint =>

  final def clearPrintln(input: Any = "")(implicit style: Ansi.Style = Reset): Unit = {
    Console println cleared(input)(style)
  }

  final def clearPrint(input: Any = "")(implicit style: Ansi.Style = Reset): Unit = {
    Console print cleared(input)(style)
  }

  final def cleared(input: Any = "")(implicit style: Ansi.Style = Reset): String =
    styled(ClearPrint.Clear.value + s"\r$input" + Reset.value)(style)

  object ClearPrint {
    final lazy val Clear = "\u001b[2K".toAnsiStyle
  }
}
