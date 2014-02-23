package spells

object Ansi extends Ansi {
  final class AnsiString(val input: Any) extends AnyVal {
    def in(style: Ansi#AnsiStyle): String = style.value + noStyles(String valueOf input) + Reset.value

    def black: String = this in Black
    def red: String = this in Red
    def green: String = this in Green
    def yellow: String = this in Yellow
    def blue: String = this in Blue
    def magenta: String = this in Magenta;
    def cyan: String = this in Cyan
    def white: String = this in White

    def bold: String = this in Bold
    def blink: String = this in Blink
    def reversed: String = this in Reversed
    def invisible: String = this in Invisible
  }

  private def noStyles(input: String) = input.replaceAll(StylePrint.styleOrReset, "")

  private lazy val Sample = "sample"
}

trait Ansi {
  import Ansi._

  implicit final def anyToAnsiString(input: Any): AnsiString = new AnsiString(input)

  implicit final class AnsiStyleWrapper(style: String) {
    def toAnsiStyle: AnsiStyle = AnsiStyle(style)
  }

  case class AnsiStyle(value: String) {
    override def toString = Ansi.Sample in this
  }

  final lazy val Reset: AnsiStyle = Console.RESET.toAnsiStyle
  final lazy val Black: AnsiStyle = Console.BLACK.toAnsiStyle
  final lazy val Red: AnsiStyle = Console.RED.toAnsiStyle
  final lazy val Green: AnsiStyle = Console.GREEN.toAnsiStyle
  final lazy val Yellow: AnsiStyle = Console.YELLOW.toAnsiStyle
  final lazy val Blue: AnsiStyle = Console.BLUE.toAnsiStyle
  final lazy val Magenta: AnsiStyle = Console.MAGENTA.toAnsiStyle
  final lazy val Cyan: AnsiStyle = Console.CYAN.toAnsiStyle
  final lazy val White: AnsiStyle = Console.WHITE.toAnsiStyle

  final lazy val Bold: AnsiStyle = Console.BOLD.toAnsiStyle
  final lazy val Blink: AnsiStyle = Console.BLINK.toAnsiStyle
  final lazy val Reversed: AnsiStyle = Console.REVERSED.toAnsiStyle
  final lazy val Invisible: AnsiStyle = Console.INVISIBLE.toAnsiStyle
}
