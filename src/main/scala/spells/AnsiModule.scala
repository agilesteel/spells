package spells

trait AnsiModule {
  implicit final def anyToAnsiString(input: Any): AnsiModule.AnsiString = new AnsiModule.AnsiString(input)

  implicit final class AnsiStyleBuilder(style: String) {
    def toAnsiStyle: AnsiModule.Style = {
      require(style != null)

      style match {
        case "Random" => AnsiModule.Random
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
        case _ => new AnsiModule.Style(style)
      }
    }
  }

  final val Reset: AnsiModule.Style = Console.RESET.toAnsiStyle
  final val Black: AnsiModule.Style = Console.BLACK.toAnsiStyle
  final val Red: AnsiModule.Style = Console.RED.toAnsiStyle
  final val Green: AnsiModule.Style = Console.GREEN.toAnsiStyle
  final val Yellow: AnsiModule.Style = Console.YELLOW.toAnsiStyle
  final val Blue: AnsiModule.Style = Console.BLUE.toAnsiStyle
  final val Magenta: AnsiModule.Style = Console.MAGENTA.toAnsiStyle
  final val Cyan: AnsiModule.Style = Console.CYAN.toAnsiStyle
  final val White: AnsiModule.Style = Console.WHITE.toAnsiStyle
}

object AnsiModule extends AnsiModule {
  class Style(val value: String) extends AnyVal {
    override final def toString: String = AnsiModule.Sample in this
  }

  final class AnsiString(val input: Any) extends AnyVal {
    final def in(style: AnsiModule.Style): String = style.value + removedStyles(String valueOf input) + Reset.value

    final def black: String = this in Black
    final def red: String = this in Red
    final def green: String = this in Green
    final def yellow: String = this in Yellow
    final def blue: String = this in Blue
    final def magenta: String = this in Magenta
    final def cyan: String = this in Cyan
    final def white: String = this in White
  }

  final def removedStyles(input: String): String = input.replaceAll(StylePrint.StyleOrReset, "")

  private final val Sample: String = "sample"

  final def Random: AnsiModule.Style =
    AnsiModule.AllStylesOutOfTheBox {
      util.Random.nextInt {
        AnsiModule.AllStylesOutOfTheBox.size
      }
    }

  val AllStylesOutOfTheBox: Vector[Style] =
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
