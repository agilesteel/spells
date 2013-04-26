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
    def in(style: AnsiPrint#AnsiStyle): String = style + noStyles(input.toString) + Reset
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

  final def printerr(error: Any): Unit = {
    println(error)(Red)
  }

  final def println(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console.println(styled(input.toString)(style))
  }

  final def print(input: Any = "")(implicit style: AnsiStyle = Reset): Unit = {
    Console.print(styled(input.toString)(style))
  }

  private[spells] def styled(input: String)(implicit style: AnsiStyle = Reset): String =
    if (style == Reset || input.matches(AnsiPattern.toString)) input
    else restyle(input, style)

  private def restyle(input: String, style: AnsiStyle): String = input match {
    case AnsiPattern() => input.replaceAll(styleOnly, style.toString)
    case AnsiPatterns(_) => input
    case StuffFollowedByAnsiPatterns(stuff, ansi) => restyle(stuff, style) + ansi
    case AnsiPatternsFollowedByStuff(_, stuff) => input.dropRight(stuff.size) + restyle(stuff, style)
    case StuffFollowedByAnsiPatternsFollowedByStuff(start, ansi, end) => restyle(start, style) + ansi + restyle(end, style)
    case _ => style + input + Reset
  }
}