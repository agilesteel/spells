package spells

private[spells] abstract class LocationAwareProperty[T](implicit materialised: (LocationAwareProperty[T] => T)) {
  private[spells] lazy val value: T = materialised(this)

  private[spells] lazy val location: String =
    scala.reflect.NameTransformer
      .decode(getClass.getName)
      .replace(".package", "")
      .split('$')
      .mkString(".")

  override def toString: String = value.toString
}
