package spells

class StringOpsTests extends UnitTestConfiguration {
  test("decodeScalaSymbols should delegate to scala.reflect.NameTransformer.decode") {
    val sample = "Encoded + Whatever"
    val encodedSample = scala.reflect.NameTransformer encode sample

    encodedSample.decodeScalaSymbols should be(sample)
  }
}
