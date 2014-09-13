package spells

class AnyOpsTests extends UnitTestConfiguration {
  test("sample.decodedSimpleName should be(sample.getClass.getSimpleName.decodeScalaSymbols)") {
    val sample = (new `Encoded + Whatever`)

    sample.decodedSimpleName should be(sample.getClass.getSimpleName.decodeScalaSymbols)
  }

  test(""""sample".decodedSimpleName should be("String")""") {
    "sample".decodedSimpleName should be("String")
  }
}

class `Encoded + Whatever`
