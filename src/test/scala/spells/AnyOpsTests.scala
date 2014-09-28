package spells

class AnyOpsTests extends UnitTestConfiguration {
  test("sample.decodedSimpleName should be(sample.getClass.getSimpleName.withDecodedScalaSymbols)") {
    forEvery(samples) { sample =>
      sample.decodedSimpleName should be(sample.getClass.getSimpleName.withDecodedScalaSymbols)
    }
  }

  test("sample.decodedName should be(sample.getClass.getName.withDecodedScalaSymbols)") {
    forEvery(samples) { sample =>
      sample.decodedName should be(sample.getClass.getName.withDecodedScalaSymbols)
    }
  }

  private val samples = Vector("sample", (new `Encoded + Whatever`))
}

class `Encoded + Whatever`
