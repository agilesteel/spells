package spells

trait AnyOps {
  this: StringOps with Ansi with StylePrint =>

  implicit class AnyOpsFromSpells(input: Any) extends CustomRendering {
    def rendered: String = String valueOf input

    def decodedSimpleClassName: String = orNull(input.getClass.getSimpleName.withDecodedScalaSymbols)
    def decodedClassName: String = orNull(input.getClass.getName.withDecodedScalaSymbols)

    private def orNull(name: => String): String =
      if (input == null) "Null" else name
  }
}
