package spells

object StylePrint {
  private[spells] lazy val AnsiPatterns = ("(" + AnsiPattern + ")+").r
  private[spells] lazy val AnsiPattern = (styleOrReset + word + reset).r

  private[spells] lazy val StuffFollowedByAnsiPatterns = ("(" + stuff + ")" + AnsiPatterns).r
  private[spells] lazy val AnsiPatternsFollowedByStuff = (AnsiPatterns + "(" + stuff + ")").r
  private[spells] lazy val StuffFollowedByAnsiPatternsFollowedByStuff = (StuffFollowedByAnsiPatterns + "(" + stuff + ")").r

  private[spells] lazy val word = """\w*"""
  private[spells] lazy val stuff = """.*"""

  private[spells] lazy val styleOnly = """\033\[\d{2}m"""
  private[spells] lazy val styleOrReset = """\033\[\d{1,2}m"""
  private[spells] lazy val reset = """\033\[0m"""
}

trait StylePrint {
  import StylePrint._
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
    case AnsiPattern() => input.replaceAll(styleOnly, style.value)
    case AnsiPatterns(_) => input
    case StuffFollowedByAnsiPatterns(stuff, ansi) => restyle(stuff, style) + ansi
    case AnsiPatternsFollowedByStuff(_, stuff) => input.dropRight(stuff.size) + restyle(stuff, style)
    case StuffFollowedByAnsiPatternsFollowedByStuff(start, ansi, end) => restyle(start, style) + ansi + restyle(end, style)
    case _ => style.value + input + Reset.value
  }
}
