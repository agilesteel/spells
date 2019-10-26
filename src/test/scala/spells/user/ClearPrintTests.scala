package spells.user

class ClearPrintTests extends spells.UnitTestConfiguration {
  test(
    """cleared(sample)(Blue) should be(styled(ClearPrint.Clear.value + s"\r$sample" + Reset.value)(Blue))"""
  ) {
    forAll(TestSamples.samples) { sample =>
      cleared(sample)(AnsiStyle.Blue) should be(
        styled(ClearPrint.Clear.value + s"\r$sample" + AnsiStyle.Reset.value)(
          AnsiStyle.Blue
        )
      )
    }
  }

  test("""Scoverage should be happy""") {
    SilentOutputStream out {
      clearPrint()
      clearPrintln()
    }
  }
}
