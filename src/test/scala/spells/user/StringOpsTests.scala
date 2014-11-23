package spells.user

class StringOpsTests extends UnitTestConfiguration {
  test("withDecodedScalaSymbols should delegate to scala.reflect.NameTransformer.decode") {
    val sample = "Encoded + Whatever"
    val encodedSample = scala.reflect.NameTransformer encode sample

    encodedSample.withDecodedScalaSymbols should be(sample)
  }
}
