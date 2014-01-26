package spells

class ErrorPrintTests extends UnitTestConfiguration {
  test("""erred(error) should be(styled(error)(Red))""") {
    forEvery(TestSamples.samples) { error =>
      erred(error) should be(styled(error)(Red))
    }
  }
}
