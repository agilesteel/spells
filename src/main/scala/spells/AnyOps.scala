package spells

trait AnyOps {
  this: StringOps =>

  implicit class AnyOps(input: Any) {
    def decodedSimpleClassName: String = orNull(input.getClass.getSimpleName.withDecodedScalaSymbols)
    def decodedClassName: String = orNull(input.getClass.getName.withDecodedScalaSymbols)

    private def orNull(name: => String): String =
      if (input != null) name else "Null"
  }
}
