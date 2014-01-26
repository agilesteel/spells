package spells

object Ansi extends Ansi {
  final class AnsiString(val input: Any) extends AnyVal {
    def in(style: Ansi#AnsiStyle): String = style.value + noStyles(String valueOf input) + Reset.value

    @inline def black: String = this in Black
    @inline def red: String = this in Red
    @inline def green: String = this in Green
    @inline def yellow: String = this in Yellow
    @inline def blue: String = this in Blue
    @inline def magenta: String = this in Magenta;
    @inline def cyan: String = this in Cyan
    @inline def white: String = this in White

    @inline def bold: String = this in Bold
    @inline def blink: String = this in Blink
    @inline def reversed: String = this in Reversed
    @inline def invisible: String = this in Invisible
  }

  private def noStyles(input: String) = input.replaceAll(StylePrint.styleOrReset, "")

  private lazy val Sample = "sample"
}

trait Ansi extends StylePrint with ErrorPrint with ClearPrint {
  import Ansi._

  implicit final def anyToAnsiString(input: Any): AnsiString = new AnsiString(input)

  implicit final class AnsiStyleWrapper(style: String) {
    def toAnsiStyle: AnsiStyle = AnsiStyle(style)
  }

  case class AnsiStyle(value: String) {
    override def toString = Ansi.Sample in this
  }

  @inline final lazy val Reset: AnsiStyle = Console.RESET.toAnsiStyle
  @inline final lazy val Black: AnsiStyle = Console.BLACK.toAnsiStyle
  @inline final lazy val Red: AnsiStyle = Console.RED.toAnsiStyle
  @inline final lazy val Green: AnsiStyle = Console.GREEN.toAnsiStyle
  @inline final lazy val Yellow: AnsiStyle = Console.YELLOW.toAnsiStyle
  @inline final lazy val Blue: AnsiStyle = Console.BLUE.toAnsiStyle
  @inline final lazy val Magenta: AnsiStyle = Console.MAGENTA.toAnsiStyle
  @inline final lazy val Cyan: AnsiStyle = Console.CYAN.toAnsiStyle
  @inline final lazy val White: AnsiStyle = Console.WHITE.toAnsiStyle

  @inline final lazy val Bold: AnsiStyle = Console.BOLD.toAnsiStyle
  @inline final lazy val Blink: AnsiStyle = Console.BLINK.toAnsiStyle
  @inline final lazy val Reversed: AnsiStyle = Console.REVERSED.toAnsiStyle
  @inline final lazy val Invisible: AnsiStyle = Console.INVISIBLE.toAnsiStyle
}
