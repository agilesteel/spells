package spells.user

class ClearPrintTests extends UnitTestConfiguration {
  test("""cleared(sample)(Blue) should be(styled(ClearPrint.Clear.value + s"\r$sample" + Reset.value)(Blue))""") {
    forAll(TestSamples.samples) { sample =>
      cleared(sample)(Blue) should be(styled(ClearPrint.Clear.value + s"\r$sample" + Reset.value)(Blue))
    }
  }

  test("""Scoverage should be happy""") {
    SilentOutputStream out {
      clearPrint()
      clearPrintln()
    }
  }
}
