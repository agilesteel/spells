package spells

private[spells] abstract class LocationAwareProperty[T](implicit materialised: (LocationAwareProperty[T] => T)) {
  final lazy val value: T = materialised(this)

  final lazy val location: String =
    scala.reflect.NameTransformer
      .decode(getClass.getName)
      .replace("SpellsConfigModule$SpellsConfig$", "")
      .split('$')
      .mkString(".")

  override final def toString: String = value.toString
}
