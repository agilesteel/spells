package spells

trait StringOps {
  implicit class StringOps(input: String) {
    def withDecodedScalaSymbols: String = scala.reflect.NameTransformer decode input
  }
}
