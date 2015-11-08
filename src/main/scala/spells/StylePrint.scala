package spells

trait StylePrint {
  this: AnsiModule =>

  import StylePrint._

  final def println(input: Any = "")(implicit style: AnsiModule.Style = Reset): Unit = {
    Console println styled(input)(style)
  }

  final def print(input: Any = "")(implicit style: AnsiModule.Style = Reset): Unit = {
    Console print styled(input)(style)
  }

  final def styled(input: Any)(implicit style: AnsiModule.Style = Reset): String = {
    val in = String valueOf input

    if (style == Reset) in
    else restyle(in, style)
  }

  private final def restyle(input: String, style: AnsiModule.Style): String = input match {
    case AnsiPattern(before, alreadyStyled, after) => restyle(before, style) + alreadyStyled + restyle(after, style)
    case _ => if (input.isEmpty) "" else style.value + input + Reset.value
  }
}

object StylePrint extends AnsiModule with StylePrint {
  private[spells] final val Multiline: String = """?s"""
  private[spells] final val Anything: String = """.*?"""

  private[spells] final val StyleOnly: String = """\033\[\d{2}m"""
  private[spells] final val StyleOrReset: String = """\033\[\d{1,2}m"""
  private[spells] final val ResetValue: String = """\033\[0m"""

  private[spells] final val AnsiPattern = s"""($Multiline)($Anything)($StyleOnly$Anything$ResetValue)($Anything)""".r
}
