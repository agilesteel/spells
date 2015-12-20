package spells

/** Contains deep (already existing styles will be preserved) styling methods.
  */
trait StylePrintModule {
  this: AnsiModule with SpellsConfigModule =>

  import scala.util.matching.Regex

  /** Prints out an object to the default output, followed by a newline character.
    * @param input the object to print
    * @param style the style for the object to be printed in
    */
  final def println(input: Any = "")(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): Unit = {
    Console println styled(input)(style)
  }

  /** Prints an object to out using its toString method.
    * @param input the object to print; may be null
    * @param style the style for the object to be printed in
    */
  final def print(input: Any = "")(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): Unit = {
    Console print styled(input)(style)
  }

  /** Styled an object in a given style. Involves deep (already existing styles will be preserved) styling.
    * {{{
    * styled("green" + "yellow".yellow + "green")(Green) // yellow is preserved
    * }}}
    * @param input the object to print
    * @param style the style for the object to be printed in
    * @return the styled object as `String`
    */
  final def styled(input: Any)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): String = {
    val rawValue = String valueOf input

    if (!SpellsConfig.terminal.display.Styles || style == AnsiStyle.Reset) rawValue
    else restyle(rawValue, style)
  }

  private final def restyle(input: String, style: AnsiModule#AnsiStyle): String = input match {
    case StylePrint.AnsiPattern(before, alreadyStyled, after) => restyle(before, style) + alreadyStyled + restyle(after, style)
    case _ => if (input.isEmpty) input else style.value + input + AnsiStyle.Reset.value
  }

  object StylePrint {
    private[spells] val Anything: String = """.*?"""
    private[spells] val Multiline: String = """?s"""

    val ResetOnly: String = """\033\[0m"""
    val StyleOnly: String = """\033\[\d{2}m"""
    val StyleOrReset: String = """\033\[\d{1,2}m"""

    val AnsiPattern: Regex = s"""($Multiline)($Anything)($StyleOnly$Anything$ResetOnly)($Anything)""".r
  }
}
