package spells

/** Provides utility methods for rendering information while remaining on the same line
  * {{{
  * import scala.concurrent._
  *
  * object SpinningWheel {
  * private var on: Boolean = false
  * private def off = !on
  *
  * def hide(): Unit = on = false
  *
  * def show()(implicit ec: ExecutionContext = ExecutionContext.Implicits.global): Unit = {
  * if (off) {
  * on = true
  *
  * Future {
  * while (on) {
  * clearPrint('/'); Thread sleep 500
  * clearPrint('-'); Thread sleep 500
  * clearPrint('\\'); Thread sleep 500
  * }
  *
  * clearPrintln()
  * }
  * }
  * }
  * }
  * }}}
  */
trait ClearPrintModule {
  this: AnsiModule with StylePrintModule =>

  /** Prints out an object to the default output, beginning at the first character in the current line of your terminal, followed by a newline character.
    * @param input the object to print
    * @param style custom `AnsiStyle`
    */
  final def clearPrintln(
      input: Any = ""
    )(
      implicit
      style: AnsiModule#AnsiStyle = AnsiStyle.Reset
    ): Unit = {
    Console println cleared(input)(style)
  }

  /** Prints an object to out using its toString method, beginning at the first character in the current line of your terminal.
    * @param input the object to print
    * @param style custom `AnsiStyle`
    */
  final def clearPrint(
      input: Any = ""
    )(
      implicit
      style: AnsiModule#AnsiStyle = AnsiStyle.Reset
    ): Unit = {
    Console print cleared(input)(style)
  }

  /** Moves the caret to the beginning of the line.
    * @param input the object to be moved to the beginning of the line
    * @param style custom `AnsiStyle`
    * @return the object, which when printing will begin at the first character in the current line of your terminal.
    */
  final def cleared(
      input: Any = ""
    )(
      implicit
      style: AnsiModule#AnsiStyle = AnsiStyle.Reset
    ): String =
    styled(ClearPrint.Clear.value + s"\r$input" + AnsiStyle.Reset.value)(style)

  object ClearPrint {
    val Clear: AnsiModule#AnsiStyle = "\u001b[2K".toAnsiStyle
  }
}
