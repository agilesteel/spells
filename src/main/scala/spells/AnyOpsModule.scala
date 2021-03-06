package spells

/** Provides utility methods for everything.
  * {{{
  * class `Encoded + Fancy` {
  * def show(): Unit = {
  * println(getClass.getName)      // Encoded$u0020$plus$u0020Fancy
  * println(this.decodedClassName) // Encoded + Fancy
  * }
  * }
  * }}}
  */
trait AnyOpsModule {
  this: StringOpsModule =>

  final implicit class AnyOpsFromSpells(input: Any) {

    /** Decodes class names.
      * @return decoded class names.
      */
    final def decodedClassName: String =
      Option(input).fold(Null)(_.getClass.getName.withDecodedScalaSymbols)

    /** Decodes class names.
      * @return decoded class names.
      */
    final def decodedSimpleClassName: String =
      try Option(input).fold(Null)(
        _.getClass.getSimpleName.withDecodedScalaSymbols
      )
      catch {
        // https://issues.scala-lang.org/browse/SI-2034
        // $COVERAGE-OFF$
        case _: Exception => decodedClassName
        // $COVERAGE-ON$
      }

    final private val Null: String = "Null"
  }
}
