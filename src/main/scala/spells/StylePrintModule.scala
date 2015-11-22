package spells

trait StylePrintModule {
  this: AnsiModule with SpellsConfigModule =>

  final def println(input: Any = "")(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): Unit = {
    Console println styled(input)(style)
  }

  final def print(input: Any = "")(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): Unit = {
    Console print styled(input)(style)
  }

  final def styled(input: Any)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): String = {
    val rawValue = String valueOf input

    if (!SpellsConfig.terminal.display.Styles || style == AnsiStyle.Reset) rawValue
    else restyle(rawValue, style)
  }

  private final def restyle(input: String, style: AnsiModule#AnsiStyle): String = input match {
    case StylePrint.AnsiPattern(before, alreadyStyled, after) => restyle(before, style) + alreadyStyled + restyle(after, style)
    case _ => if (input.isEmpty) "" else style.value + input + AnsiStyle.Reset.value
  }

  object StylePrint {
    private[spells] val Anything: String = """.*?"""
    private[spells] val Multiline: String = """?s"""

    val ResetOnly: String = """\033\[0m"""
    val StyleOnly: String = """\033\[\d{2}m"""
    val StyleOrReset: String = """\033\[\d{1,2}m"""

    val AnsiPattern = s"""($Multiline)($Anything)($StyleOnly$Anything$ResetOnly)($Anything)""".r
  }
}
