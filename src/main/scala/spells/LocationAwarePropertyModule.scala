package spells

trait LocationAwarePropertyModule {
  this: StylePrintModule with AnsiModule =>

  abstract class LocationAwareProperty[T](implicit materialised: (LocationAwareProperty[T] => T)) {
    final val value: T = {
      val resultingValue = materialised(this)
      require(isValid(resultingValue), validationErrorMessage(resultingValue))
      resultingValue
    }

    def isValid(in: T): Boolean = true
    def validationErrorMessage(in: T): String = styled {
      val errorMessage = s"SpellsConfig contains a property: ${location.yellow} with an illegal value: ${in.yellow}."
      val customErrorMessage = customValidationErrorMessage(in)

      errorMessage + " " + customErrorMessage
    }(Red)

    def customValidationErrorMessage(in: T): String = ""

    final lazy val location: String =
      scala.reflect.NameTransformer
        .decode(getClass.getName)
        .replace("SpellsConfigModule$SpellsConfig$", "")
        .split('$')
        .mkString(".")

    override final def toString: String = value.toString
  }
}
