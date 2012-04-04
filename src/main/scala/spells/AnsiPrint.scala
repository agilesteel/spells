package spells

trait AnsiPrint {
  implicit final def stringToAnsiString(s: String): AnsiString = new AnsiString(s)
  implicit final lazy private val DefaultStyle = Console.RESET

  final lazy val Reset = Console.RESET
  final lazy val Black = Console.BLACK
  final lazy val Red = Console.RED
  final lazy val Green = Console.GREEN
  final lazy val Yellow = Console.YELLOW
  final lazy val Blue = Console.BLUE
  final lazy val Magenta = Console.MAGENTA
  final lazy val Cyan = Console.CYAN
  final lazy val White = Console.WHITE

  final lazy val Bold = Console.BOLD
  final lazy val Blink = Console.BLINK
  final lazy val Reversed = Console.REVERSED
  final lazy val Invisible = Console.INVISIBLE

  final class AnsiString(input: String) {
    final def in(style: String): String = style + noStyles(input) + Reset
    final def black = this in Black
    final def red = this in Red
    final def green = this in Green
    final def yellow = this in Yellow
    final def blue = this in Blue
    final def magenta = this in Magenta;
    final def cyan = this in Cyan
    final def white = this in White
    final def bold = this in Bold
    final def blink = this in Blink
    final def reversed = this in Reversed
    final def invisible = this in Invisible
  }

  private def noStyles(input: String) = input.replaceAll(styleOrReset, "")

  final def printerr(error: Any): Unit = {
    println(error)(Red)
  }

  final def println(input: Any = "")(implicit style: String = DefaultStyle): Unit = {
    Console.println(styled(input.toString)(style))
  }

  final def print(input: Any = "")(implicit style: String = DefaultStyle): Unit = {
    Console.print(styled(input.toString)(style))
  }

  private[spells] def styled(input: String)(implicit style: String = DefaultStyle): String = style match {
    case Reset => input
    case _ => restyle(input, style)
  }

  private def restyle(input: String, style: String): String = input match {
    case AnsiPattern() => input.replaceAll(styleOnly, style)
    case AnsiPatterns(_) => input
    case WordFollowedByAnsiPatterns(word, _) => restyle(word, style) + input.drop(word.size)
    case AnsiPatternsFollowedByWord(_, word) => input.dropRight(word.size) + restyle(word, style)
    case StuffFollowedByAnsiPatterns(stuff, _) => restyle(stuff, style) + input.drop(stuff.size)
    case AnsiPatternsFollowedByStuff(_, stuff) => input.dropRight(stuff.size) + restyle(stuff, style)
    case StuffFollowedByAnsiPatternsFollowedByStuff(start, ansi, end) => restyle(start, style) + ansi + restyle(end, style)
    case AnsiPatternsFollowedByStuffFollowedByAnsiPatterns(ansiStart, stuff, ansiEnd) => ansiStart + restyle(stuff, style) + ansiEnd
    case _ => style + input + Reset
  }

  private lazy val AnsiPatterns = ("(" + AnsiPattern + ")+").r
  private lazy val AnsiPattern = (styleOrReset + word + reset).r

  private lazy val WordFollowedByAnsiPatterns = ("(" + word + ")" + AnsiPatterns).r
  private lazy val AnsiPatternsFollowedByWord = (AnsiPatterns + "(" + word + ")").r

  private lazy val StuffFollowedByAnsiPatterns = ("(" + stuff + ")" + AnsiPatterns).r
  private lazy val AnsiPatternsFollowedByStuff = (AnsiPatterns + "(" + stuff + ")").r

  private lazy val AnsiPatternsFollowedByStuffFollowedByAnsiPatterns = (AnsiPatternsFollowedByStuff + AnsiPatterns.toString).r
  private lazy val StuffFollowedByAnsiPatternsFollowedByStuff = (StuffFollowedByAnsiPatterns + "(" + stuff + ")").r

  private lazy val word = """\w*"""
  private lazy val stuff = """.*"""

  private lazy val styleOnly = """\033\[\d{2}m"""
  private lazy val styleOrReset = """\033\[\d{1,2}m"""
  private lazy val reset = """\033\[0m"""
}