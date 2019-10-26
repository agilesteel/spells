package spells

private[spells] trait LocationAwarePropertyModule {
  this: StylePrintModule with AnsiModule =>

  abstract class LocationAwareProperty[T](
      implicit
      materialised: (LocationAwareProperty[T] => T)) {
    final lazy val value: T = materialised(this)

    def isValid: T => Boolean = _ => true
    def validationErrorMessage(in: T): String =
      styled {
        val errorMessage =
          s"SpellsConfig contains a property: ${location.yellow} with an illegal value: ${in.yellow}"
        val customErrorMessage = customValidationErrorMessage(in)

        if (customErrorMessage.isEmpty) errorMessage
        else errorMessage + ": " + customErrorMessage
      }(AnsiStyle.Red)

    def customValidationErrorMessage: T => String = _ => ""

    final lazy val location: String =
      scala.reflect.NameTransformer
        .decode(getClass.getName)
        .replace("SpellsConfigModule$SpellsConfig$", "")
        .split('$')
        .mkString(".")

    final override def toString: String = value.toString
  }
}
