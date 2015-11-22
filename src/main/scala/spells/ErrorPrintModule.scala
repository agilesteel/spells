package spells

trait ErrorPrintModule {
  this: AnsiModule with StylePrintModule =>

  final def printerr(error: Any): Unit = {
    Console.err println erred(error)
  }

  final def erred(error: Any): String = styled(error)(AnsiStyle.Red)
}
