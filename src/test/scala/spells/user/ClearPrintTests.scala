package spells.user

class ClearPrintTests extends UnitTestConfiguration {
  test("""cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(Clear))(Blue))""") {
    forEvery(TestSamples.samples) { sample =>
      cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(ClearPrint.Clear))(Blue))
    }
  }

  test("""Scoverage should be happy""") {
    SilentOutputStream out {
      clearPrint()
      clearPrintln()
    }
  }
}
