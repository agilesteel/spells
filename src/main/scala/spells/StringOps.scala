package spells

trait StringOps {
  implicit final def stringToStringOps(input: String): StringOps.StringOps = new StringOps.StringOps(input)
}

object StringOps {
  class StringOps(input: String) {
    def withDecodedScalaSymbols: String = scala.reflect.NameTransformer decode input
  }
}
