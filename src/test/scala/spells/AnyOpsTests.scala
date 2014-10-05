package spells

class AnyOpsTests extends UnitTestConfiguration {
  test("sample.decodedSimpleClassName should be(sample.getClass.getSimpleName.withDecodedScalaSymbols)") {
    forEvery(samples) { sample =>
      sample.decodedSimpleClassName should be(sample.getClass.getSimpleName.withDecodedScalaSymbols)
    }
  }

  test("sample.decodedClassName should be(sample.getClass.getName.withDecodedScalaSymbols)") {
    forEvery(samples) { sample =>
      sample.decodedClassName should be(sample.getClass.getName.withDecodedScalaSymbols)
    }
  }

  private val samples = Vector("sample", (new `Encoded + Whatever`))

  test("Null should not be an issue") {
    (null: String).decodedSimpleClassName should be("Null")
    (null: String).decodedClassName should be("Null")
  }
}

class `Encoded + Whatever` {
  override def toString = "encoded"
}
