package spells

trait AnyOps {
  implicit final def anyToClassOps(input: Any): AnyOps.AnyOps = new AnyOps.AnyOps(input)
}

object AnyOps {
  class AnyOps(input: Any) {
    def decodedSimpleClassName: String = orNull(input.getClass.getSimpleName.withDecodedScalaSymbols)
    def decodedClassName: String = orNull(input.getClass.getName.withDecodedScalaSymbols)

    private def orNull(name: => String): String =
      if (input != null) name else "Null"
  }
}
