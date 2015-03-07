package spells

trait StylePrint {
  this: Ansi =>

  import StylePrint._

  final def println(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console println styled(input)(style)
  }

  final def print(input: Any = "")(implicit style: Ansi#AnsiStyle = Reset): Unit = {
    Console print styled(input)(style)
  }

  final def styled(anput: Any)(implicit style: Ansi#AnsiStyle = Reset): String = {
    val input = String valueOf anput

    if (style == Reset) input
    else restyle(input, style)
  }

  private def restyle(input: String, style: Ansi#AnsiStyle): String = input match {
    case AnsiPattern(before, alreadyStyled, after) => restyle(before, style) + alreadyStyled + restyle(after, style)
    case _ => if (input.isEmpty) "" else style.value + input + Reset.value
  }
}

object StylePrint extends Ansi with StylePrint {
  private[spells] val AnsiPattern = s"""($multiline)($anything)($styleOnly$anything$reset)($anything)""".r

  private[spells] lazy val multiline = """?s"""
  private[spells] lazy val anything = """.*?"""

  private[spells] lazy val styleOnly = """\033\[\d{2}m"""
  private[spells] lazy val styleOrReset = """\033\[\d{1,2}m"""
  private[spells] lazy val reset = """\033\[0m"""
}
