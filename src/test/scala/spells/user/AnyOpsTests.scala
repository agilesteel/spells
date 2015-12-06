package spells.user

class AnyOpsTests extends spells.UnitTestConfiguration {
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

  test("Null should not be an issue when decoding classes") {
    (null: String).decodedSimpleClassName should be("Null")
    (null: String).decodedClassName should be("Null")
  }

  // Does not throw anything under Scala 2.10.6
  // test("https://issues.scala-lang.org/browse/SI-2034") {
  //   object outer {
  //     class inner
  //   }

  //   val sample = new outer.inner

  //   the[InternalError] thrownBy {
  //     sample.getClass.getSimpleName
  //   } should have message "Malformed class name"

  //   sample.decodedSimpleClassName should be(sample.decodedClassName)
  // }
}

class `Encoded + Whatever` {
  override def toString = "encoded"
}
