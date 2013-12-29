package spells

import AnsiPrint._

object AnsiPrint extends AnsiPrint {
  private lazy val AnsiPatterns = ("(" + AnsiPattern + ")+").r
  private lazy val AnsiPattern = (styleOrReset + word + reset).r

  private lazy val StuffFollowedByAnsiPatterns = ("(" + stuff + ")" + AnsiPatterns).r
  private lazy val AnsiPatternsFollowedByStuff = (AnsiPatterns + "(" + stuff + ")").r
  private lazy val StuffFollowedByAnsiPatternsFollowedByStuff = (StuffFollowedByAnsiPatterns + "(" + stuff + ")").r

  private lazy val word = """\w*"""
  private lazy val stuff = """.*"""

  private lazy val styleOnly = """\033\[\d{2}m"""
  private lazy val styleOrReset = """\033\[\d{1,2}m"""
  private lazy val reset = """\033\[0m"""

  final class AnsiString(val input: Any) extends AnyVal {
    def in(style: AnsiPrint#AnsiStyle): String = style + noStyles(String valueOf input) + Reset
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

  private def noStyles(input: String) = input.replaceAll(styleOrReset, "")
}

trait AnsiPrint {
  implicit def anyToAnsiString(input: Any) = new AnsiString(input)

  implicit final class AnsiStyleWrapper(style: String) {
    def toAnsiStyle: AnsiStyle = AnsiStyle(style)
  }

  case class AnsiStyle(value: String) {
    override def toString = value
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

  @inline final def printerr(error: Any): Unit = {
    Console.err println styled(error)(Red)
  }

  @inline final def println(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console println styled(input)(style)
  }

  @inline final def print(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console print styled(input)(style)
  }

  final def styled(anput: Any)(implicit style: AnsiStyle = Reset): String = {
    val input = String valueOf anput
    if (style == Reset || input.matches(AnsiPattern.toString)) input
    else restyle(input, style)
  }

  private def restyle(input: String, style: AnsiStyle): String = input match {
    case AnsiPattern() => input.replaceAll(styleOnly, style.toString)
    case AnsiPatterns(_) => input
    case StuffFollowedByAnsiPatterns(stuff, ansi) => restyle(stuff, style) + ansi
    case AnsiPatternsFollowedByStuff(_, stuff) => input.dropRight(stuff.size) + restyle(stuff, style)
    case StuffFollowedByAnsiPatternsFollowedByStuff(start, ansi, end) => restyle(start, style) + ansi + restyle(end, style)
    case _ => style + input + Reset
  }
}
