package spells

/** Provides a utility method which writes to the `err` stream in `AnisStyle.Red`. */
trait ErrorPrintModule {
  this: AnsiModule with StylePrintModule =>

  /** Write to the `err` stream in `AnsiStyle.Red`.
    * @param error which will be written to the stream
    */
  final def printerr(error: Any): Unit = {
    Console.err println erred(error)
  }

  /** Styles `Any in Red`.
    * @param error which will be styled
    * @return
    */
  final def erred(error: Any): String = styled(error)(AnsiStyle.Red)
}
