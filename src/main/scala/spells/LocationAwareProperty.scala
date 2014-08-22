package spells

private[spells] trait LocationAwareProperty[T] {
  private[spells] val location: String =
    scala.reflect.NameTransformer
      .decode(getClass.getName)
      .replace(".package", "")
      .split('$')
      .mkString(".")
}
