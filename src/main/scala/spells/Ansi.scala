package spells

trait Ansi {
  import Ansi._

  implicit final def anyToAnsiString(input: Any): AnsiString = new AnsiString(input)

  implicit final class AnsiStyleWrapper(style: String) {
    def toAnsiStyle: Ansi.Style = Ansi.Style(style)
  }

  final lazy val Reset: Ansi.Style = Console.RESET.toAnsiStyle
  final lazy val Black: Ansi.Style = Console.BLACK.toAnsiStyle
  final lazy val Red: Ansi.Style = Console.RED.toAnsiStyle
  final lazy val Green: Ansi.Style = Console.GREEN.toAnsiStyle
  final lazy val Yellow: Ansi.Style = Console.YELLOW.toAnsiStyle
  final lazy val Blue: Ansi.Style = Console.BLUE.toAnsiStyle
  final lazy val Magenta: Ansi.Style = Console.MAGENTA.toAnsiStyle
  final lazy val Cyan: Ansi.Style = Console.CYAN.toAnsiStyle
  final lazy val White: Ansi.Style = Console.WHITE.toAnsiStyle

  final lazy val Bold: Ansi.Style = Console.BOLD.toAnsiStyle
  final lazy val Blink: Ansi.Style = Console.BLINK.toAnsiStyle
  final lazy val Reversed: Ansi.Style = Console.REVERSED.toAnsiStyle
  final lazy val Invisible: Ansi.Style = Console.INVISIBLE.toAnsiStyle
}

object Ansi extends Ansi {
  case class Style(value: String) {
    override def toString = Ansi.Sample in this
  }

  final class AnsiString(val input: Any) extends AnyVal {
    def in(style: Ansi.Style): String = style.value + removeStyles(String valueOf input) + Reset.value

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

  def removeStyles(input: String) = input.replaceAll(StylePrint.styleOrReset, "")

  private lazy val Sample = "sample"
}
