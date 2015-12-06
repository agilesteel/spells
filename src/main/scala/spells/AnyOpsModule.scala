package spells

/**
 * Provides utility methods for everything.
 * {{{
 * class `Encoded + Fancy` {
 *   def show(): Unit = {
 *     println(this.getClass.getName) // Encoded$u0020$plus$u0020Fancy
 *     println(this.decodedClassName) // Encoded + Fancy
 *   }
 * }
 * }}}
 */
trait AnyOpsModule {
  this: StringOpsModule =>

  implicit final class AnyOpsFromSpells(input: Any) {
    /**
     * Decodes class names.
     * @return decoded class names.
     */
    final def decodedClassName: String =
      Option(input).fold(Null)(_.getClass.getName.withDecodedScalaSymbols)

    /**
     * Decodes class names.
     * @return decoded class names.
     */
    final def decodedSimpleClassName: String =
      try Option(input).fold(Null)(_.getClass.getSimpleName.withDecodedScalaSymbols)
      catch {
        // https://issues.scala-lang.org/browse/SI-2034
        case bug: InternalError => decodedClassName
      }

    private final val Null: String = "Null"
  }
}
