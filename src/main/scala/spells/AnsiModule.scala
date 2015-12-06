package spells

/**
 * Provides utilities, which help encoding anything with Ansi Styles.
 * @see https://en.wikipedia.org/wiki/ANSI_escape_code
 * {{{
 * println(1234.green)
 * }}}
 */
trait AnsiModule {
  this: SpellsConfigModule with StylePrintModule =>

  /**
   * Provides method `toAnsiStyle` which converts ansi codes to `AnsiStyle`.
   * Understands human readable styles like "Green" or "Random".
   * @see https://en.wikipedia.org/wiki/ANSI_escape_code
   * @param style the `String` to convert
   */
  implicit final class AnsiStyleBuilder(style: String) {
    /**
     * Converts ansi codes to `AnsiStyle`.
     * Understands human readable styles like "Green" or "Random".
     * @see https://en.wikipedia.org/wiki/ANSI_escape_code
     * @return an instance of `AnsiStyle`
     */
    final def toAnsiStyle: AnsiModule#AnsiStyle = style match {
      case "Black" => AnsiStyle.Black
      case "Blue" => AnsiStyle.Blue
      case "Cyan" => AnsiStyle.Cyan
      case "Green" => AnsiStyle.Green
      case "Magenta" => AnsiStyle.Magenta
      case "Random" => AnsiStyle.Random
      case "Red" => AnsiStyle.Red
      case "Untouched" => AnsiStyle.Reset
      case "White" => AnsiStyle.White
      case "Yellow" => AnsiStyle.Yellow
      case _ => new AnsiStyle(Option(style).fold("")(_.replace("\\033", "\u001b")))
    }
  }

  /**
   * Encoding of `AnsiStyle`s
   * @param value the style to encode
   */
  final class AnsiStyle private[spells] (val value: String) {
    override final def toString: String = AnsiStyle.Sample in this
  }

  /**
   * Provides the method `in` which converts `Any` to `String` `in` `AnsiStyle`.
   * Also provides convenience methods like `green`, `yellow` etc.
   * @param input
   */
  implicit final class AnsiString(val input: Any) {
    /**
     * Converts `Any` to `String` `in` `AnsiStyle`.
     * @param style the style to convert to
     * @return an Ansi encoded String.
     */
    final def in(style: AnsiModule#AnsiStyle): String = {
      val rawValue = String valueOf input

      if (SpellsConfig.terminal.display.Styles)
        style.value + AnsiStyle.removed(rawValue) + AnsiStyle.Reset.value
      else rawValue
    }

    final def black: String = this in AnsiStyle.Black
    final def blue: String = this in AnsiStyle.Blue
    final def cyan: String = this in AnsiStyle.Cyan
    final def green: String = this in AnsiStyle.Green
    final def magenta: String = this in AnsiStyle.Magenta
    final def red: String = this in AnsiStyle.Red
    final def white: String = this in AnsiStyle.White
    final def yellow: String = this in AnsiStyle.Yellow
  }

  /**
   * Provides out-of-the-box `AnsiStyle`s as well as a utility method to remove them.
   */
  object AnsiStyle {
    private[spells] final val Sample: String = "sample"

    /**
     * Removes `AnsiStyle`s from `String`s
     * @param input the `String` to remove `AnsiStyle` from
     * @return the `String` with the `removed` `AnsiStyle`
     */
    final def removed(input: String): String = input.replaceAll(StylePrint.StyleOrReset, "")

    final def Random: AnsiModule#AnsiStyle = All(util.Random.nextInt(All.size))
    final val Black: AnsiModule#AnsiStyle = Console.BLACK.toAnsiStyle
    final val Blue: AnsiModule#AnsiStyle = Console.BLUE.toAnsiStyle
    final val Cyan: AnsiModule#AnsiStyle = Console.CYAN.toAnsiStyle
    final val Green: AnsiModule#AnsiStyle = Console.GREEN.toAnsiStyle
    final val Magenta: AnsiModule#AnsiStyle = Console.MAGENTA.toAnsiStyle
    final val Red: AnsiModule#AnsiStyle = Console.RED.toAnsiStyle
    final val Reset: AnsiModule#AnsiStyle = Console.RESET.toAnsiStyle
    final val White: AnsiModule#AnsiStyle = Console.WHITE.toAnsiStyle
    final val Yellow: AnsiModule#AnsiStyle = Console.YELLOW.toAnsiStyle

    /**
     * `All` `AnsiStyle`s provided out-of-the-box.
     */
    final val All: Vector[AnsiModule#AnsiStyle] =
      Vector(
        Black,
        Blue,
        Cyan,
        Green,
        Magenta,
        Red,
        White,
        Yellow
      )
  }
}
