package spells

trait AnyOpsModule {
  this: StringOps =>

  implicit class AnyOpsFromSpells(input: Any) {
    final def decodedSimpleClassName: String = orNull(input.getClass.getSimpleName.withDecodedScalaSymbols)
    final def decodedClassName: String = orNull(input.getClass.getName.withDecodedScalaSymbols)

    private final def orNull(name: => String): String =
      if (input == null) "Null" else name
  }
}
