package spells

trait Ansi {
  implicit final def anyToAnsiString(input: Any): Ansi.AnsiString = new Ansi.AnsiString(input)

  implicit final class AnsiStyleWrapper(style: String) {
    def toAnsiStyle: Ansi.Style = Ansi.Style(style)
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

  final val Bold: Ansi.Style = Console.BOLD.toAnsiStyle
  final val Blink: Ansi.Style = Console.BLINK.toAnsiStyle
  final val Reversed: Ansi.Style = Console.REVERSED.toAnsiStyle
  final val Invisible: Ansi.Style = Console.INVISIBLE.toAnsiStyle
}

object Ansi extends Ansi {
  case class Style(value: String) {
    override final def toString = Ansi.Sample in this
  }

  final class AnsiString(val input: Any) extends AnyVal {
    final def in(style: Ansi.Style): String = style.value + removeStyles(String valueOf input) + Reset.value

    final def black: String = this in Black
    final def red: String = this in Red
    final def green: String = this in Green
    final def yellow: String = this in Yellow
    final def blue: String = this in Blue
    final def magenta: String = this in Magenta;
    final def cyan: String = this in Cyan
    final def white: String = this in White

    final def bold: String = this in Bold
    final def blink: String = this in Blink
    final def reversed: String = this in Reversed
    final def invisible: String = this in Invisible
  }

  final def removeStyles(input: String) = input.replaceAll(StylePrint.styleOrReset, "")

  private final lazy val Sample = "sample"
}
