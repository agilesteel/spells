package spells

trait AnsiPrint {
  implicit final def stringToAnsiString(s: String): AnsiString = new AnsiString(s)

  final class AnsiString(input: String) {
    def in(style: String): String = style + noStyles(input) + Reset
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

  final lazy val Reset: String = Console.RESET
  final lazy val Black: String = Console.BLACK
  final lazy val Red: String = Console.RED
  final lazy val Green: String = Console.GREEN
  final lazy val Yellow: String = Console.YELLOW
  final lazy val Blue: String = Console.BLUE
  final lazy val Magenta: String = Console.MAGENTA
  final lazy val Cyan: String = Console.CYAN
  final lazy val White: String = Console.WHITE

  final lazy val Bold: String = Console.BOLD
  final lazy val Blink: String = Console.BLINK
  final lazy val Reversed: String = Console.REVERSED
  final lazy val Invisible: String = Console.INVISIBLE

  final def printerr(error: Any): Unit = {
    println(error)(Red)
  }

  final def println(input: Any = "")(implicit style: String = Reset): Unit = {
    Console.println(styled(input.toString)(style))
  }

  final def print(input: Any = "")(implicit style: String = Reset): Unit = {
    Console.print(styled(input.toString)(style))
  }

  private[spells] def styled(input: String)(implicit style: String = Reset): String = style match {
    case Reset => input
    case _ => restyle(input, style)
  }

  private def restyle(input: String, style: String): String = input match {
    case AnsiPattern() => input.replaceAll(styleOnly, style)
    case AnsiPatterns(_) => input
    case StuffFollowedByAnsiPatterns(stuff, ansi) => restyle(stuff, style) + ansi
    case AnsiPatternsFollowedByStuff(_, stuff) => input.dropRight(stuff.size) + restyle(stuff, style)
    case StuffFollowedByAnsiPatternsFollowedByStuff(start, ansi, end) => restyle(start, style) + ansi + restyle(end, style)
    case _ => style + input + Reset
  }

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
}