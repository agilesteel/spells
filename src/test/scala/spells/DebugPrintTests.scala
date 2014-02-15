package spells

class DebugPrintTests extends UnitTestConfiguration {
  test("""stackTraced(sample)(Blue) should be(styled(s"$sample at ${Thread.currentThread.getStackTrace apply 1}")(Blue))""") {
    forEvery(TestSamples.samples)(assert)
  }

  private def assert(sample: String) =
    stackTraced(sample)(Blue) should be(styled(s"$sample at ${Thread.currentThread.getStackTrace apply 1}")(Blue))
}
