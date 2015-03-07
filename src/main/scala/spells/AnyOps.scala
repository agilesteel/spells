package spells

trait AnyOps {
  this: StringOps with Ansi with StylePrint =>

  implicit class AnyOps[T](input: => T) {
    def decodedSimpleClassName: String = orNull(input.getClass.getSimpleName.withDecodedScalaSymbols)
    def decodedClassName: String = orNull(input.getClass.getName.withDecodedScalaSymbols)

    private def orNull(name: => String): String =
      if (input == null) "Null" else name

    def println(implicit style: Ansi#AnsiStyle = Reset): T = {
      StylePrint.println(input)(style)

      input
    }

    def print(implicit style: Ansi#AnsiStyle = Reset): T = {
      StylePrint.print(input)(style)

      input
    }
  }
}
