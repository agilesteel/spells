package spells

trait AnyOps {
  implicit final def anyToClassOps(input: Any): AnyOps.AnyOps = new AnyOps.AnyOps(input)
}

object AnyOps {
  class AnyOps(input: Any) {
    def decodedSimpleName: String = input.getClass.getSimpleName.decodeScalaSymbols
  }
}
