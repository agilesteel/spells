package spells

trait AnsiModule {
  implicit final class AnsiStyleBuilder(style: String) {
    def toAnsiStyle: AnsiModule#AnsiStyle = {
      require(style != null)

      style match {
        case "Random" => Ansi.RandomStyle
        case "Untouched" => Reset
        case "Reset" => Reset
        case "Black" => Black
        case "Red" => Red
        case "Green" => Green
        case "Yellow" => Yellow
        case "Blue" => Blue
        case "Magenta" => Magenta
        case "Cyan" => Cyan
        case "White" => White
        case _ => new AnsiStyle(style)
      }
    }
  }

  final val Reset: AnsiModule#AnsiStyle = Console.RESET.toAnsiStyle
  final val Black: AnsiModule#AnsiStyle = Console.BLACK.toAnsiStyle
  final val Red: AnsiModule#AnsiStyle = Console.RED.toAnsiStyle
  final val Green: AnsiModule#AnsiStyle = Console.GREEN.toAnsiStyle
  final val Yellow: AnsiModule#AnsiStyle = Console.YELLOW.toAnsiStyle
  final val Blue: AnsiModule#AnsiStyle = Console.BLUE.toAnsiStyle
  final val Magenta: AnsiModule#AnsiStyle = Console.MAGENTA.toAnsiStyle
  final val Cyan: AnsiModule#AnsiStyle = Console.CYAN.toAnsiStyle
  final val White: AnsiModule#AnsiStyle = Console.WHITE.toAnsiStyle

  final class AnsiStyle(val value: String) {
    override final def toString: String = Ansi.Sample in this
  }

  implicit final class AnsiString(val input: Any) {
    final def in(style: AnsiModule#AnsiStyle): String = style.value + Ansi.removedStyles(String valueOf input) + Reset.value

    final def black: String = this in Black
    final def red: String = this in Red
    final def green: String = this in Green
    final def yellow: String = this in Yellow
    final def blue: String = this in Blue
    final def magenta: String = this in Magenta
    final def cyan: String = this in Cyan
    final def white: String = this in White
  }

  object Ansi {
    private[spells] final val Sample: String = "sample"

    final def removedStyles(input: String): String = input.replaceAll(StylePrint.StyleOrReset, "")

    final def RandomStyle: AnsiModule#AnsiStyle =
      AllStylesOutOfTheBox {
        util.Random.nextInt {
          AllStylesOutOfTheBox.size
        }
      }

    private[spells] final val AllStylesOutOfTheBox: Vector[AnsiModule#AnsiStyle] =
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
