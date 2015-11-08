package spells

trait ErrorPrint {
  this: AnsiModule with StylePrint =>

  final def printerr(error: Any): Unit = {
    Console.err println erred(error)
  }

  final def erred(error: Any): String = styled(error)(Red)
}
