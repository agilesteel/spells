package spells

trait ClearPrintModule {
  this: AnsiModule with StylePrintModule =>

  final def clearPrintln(input: Any = "")(implicit style: AnsiModule#AnsiStyle = Reset): Unit = {
    Console println cleared(input)(style)
  }

  final def clearPrint(input: Any = "")(implicit style: AnsiModule#AnsiStyle = Reset): Unit = {
    Console print cleared(input)(style)
  }

  final def cleared(input: Any = "")(implicit style: AnsiModule#AnsiStyle = Reset): String =
    styled(ClearPrint.Clear.value + s"\r$input" + Reset.value)(style)

  object ClearPrint {
    final val Clear: AnsiModule#AnsiStyle = "\u001b[2K".toAnsiStyle
  }
}
