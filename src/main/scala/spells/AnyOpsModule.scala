package spells

trait AnyOpsModule {
  this: StringOpsModule =>

  implicit final class AnyOpsFromSpells(input: Any) {
    final def decodedClassName: String =
      Option(input).fold(Null)(_.getClass.getName.withDecodedScalaSymbols)

    final def decodedSimpleClassName: String =
      Option(input).fold(Null)(_.getClass.getSimpleName.withDecodedScalaSymbols)

    private final val Null: String = "Null"
  }
}
