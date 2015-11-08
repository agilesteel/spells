package spells.user

class ErrorPrintTests extends spells.UnitTestConfiguration {
  test("""erred(error) should be(styled(error)(Red))""") {
    forEvery(TestSamples.samples) { error =>
      erred(error) should be(styled(error)(Red))
    }
  }

  test("""Scoverage should be happy""") {
    SilentOutputStream err {
      printerr("")
    }
  }
}
