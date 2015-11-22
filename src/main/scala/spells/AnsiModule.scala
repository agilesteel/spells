package spells

trait AnsiModule {
  this: SpellsConfigModule with StylePrintModule =>

  implicit final class AnsiStyleBuilder(style: String) {
    final def toAnsiStyle: AnsiModule#AnsiStyle = style match {
      case "Untouched" => AnsiStyle.Reset
      case "Random" => AnsiStyle.Random
      case "Black" => AnsiStyle.Black
      case "Red" => AnsiStyle.Red
      case "Green" => AnsiStyle.Green
      case "Yellow" => AnsiStyle.Yellow
      case "Blue" => AnsiStyle.Blue
      case "Magenta" => AnsiStyle.Magenta
      case "Cyan" => AnsiStyle.Cyan
      case "White" => AnsiStyle.White
      case _ => new AnsiStyle(Option(style).fold("")(_.replace("\\033", "\u001b")))
    }
  }

  final class AnsiStyle(val value: String) {
    override final def toString: String = AnsiStyle.Sample in this
  }

  implicit final class AnsiString(val input: Any) {
    final def in(style: AnsiModule#AnsiStyle): String = {
      val rawValue = String valueOf input

      if (SpellsConfig.terminal.display.Styles)
        style.value + AnsiStyle.removed(rawValue) + AnsiStyle.Reset.value
      else rawValue
    }

    final def black: String = this in AnsiStyle.Black
    final def red: String = this in AnsiStyle.Red
    final def green: String = this in AnsiStyle.Green
    final def yellow: String = this in AnsiStyle.Yellow
    final def blue: String = this in AnsiStyle.Blue
    final def magenta: String = this in AnsiStyle.Magenta
    final def cyan: String = this in AnsiStyle.Cyan
    final def white: String = this in AnsiStyle.White
  }

  object AnsiStyle {
    private[spells] final val Sample: String = "sample"

    final def removed(input: String): String = input.replaceAll(StylePrint.StyleOrReset, "")

    final val Reset: AnsiModule#AnsiStyle = Console.RESET.toAnsiStyle
    final val Black: AnsiModule#AnsiStyle = Console.BLACK.toAnsiStyle
    final val Red: AnsiModule#AnsiStyle = Console.RED.toAnsiStyle
    final val Green: AnsiModule#AnsiStyle = Console.GREEN.toAnsiStyle
    final val Yellow: AnsiModule#AnsiStyle = Console.YELLOW.toAnsiStyle
    final val Blue: AnsiModule#AnsiStyle = Console.BLUE.toAnsiStyle
    final val Magenta: AnsiModule#AnsiStyle = Console.MAGENTA.toAnsiStyle
    final val Cyan: AnsiModule#AnsiStyle = Console.CYAN.toAnsiStyle
    final val White: AnsiModule#AnsiStyle = Console.WHITE.toAnsiStyle
    final def Random: AnsiModule#AnsiStyle = All(util.Random.nextInt(All.size))

    final val All: Vector[AnsiModule#AnsiStyle] =
      Vector(
        Black,
        Red,
        Green,
        Yellow,
        Blue,
        Magenta,
        Cyan,
        White
      )
  }
}
