package spells

trait Ansi {
  implicit final def anyToAnsiString(input: Any): Ansi.AnsiString = new Ansi.AnsiString(input)

  implicit final class AnsiStyleBuilder(style: String) {
    def toAnsiStyle: Ansi.Style = {
      require(style != null)

      style match {
        case "Random" => Ansi.Random
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
        case _ => new Ansi.Style(style)
      }
    }
  }

  final val Reset: Ansi.Style = Console.RESET.toAnsiStyle
  final val Black: Ansi.Style = Console.BLACK.toAnsiStyle
  final val Red: Ansi.Style = Console.RED.toAnsiStyle
  final val Green: Ansi.Style = Console.GREEN.toAnsiStyle
  final val Yellow: Ansi.Style = Console.YELLOW.toAnsiStyle
  final val Blue: Ansi.Style = Console.BLUE.toAnsiStyle
  final val Magenta: Ansi.Style = Console.MAGENTA.toAnsiStyle
  final val Cyan: Ansi.Style = Console.CYAN.toAnsiStyle
  final val White: Ansi.Style = Console.WHITE.toAnsiStyle
}

object Ansi extends Ansi {
  class Style(val value: String) extends AnyVal {
    override final def toString: String = Ansi.Sample in this
  }

  final class AnsiString(val input: Any) extends AnyVal {
    final def in(style: Ansi.Style): String = style.value + removedStyles(String valueOf input) + Reset.value

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

  final def Random: Ansi.Style =
    Ansi.AllStylesOutOfTheBox {
      util.Random.nextInt {
        Ansi.AllStylesOutOfTheBox.size
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
