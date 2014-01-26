package spells

class ClearPrintTests extends UnitTestConfiguration {
  test("""cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(Clear))(Blue))""") {
    forEvery(TestSamples.samples) { sample =>
      cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(ClearPrint.Clear))(Blue))
    }
  }
}
