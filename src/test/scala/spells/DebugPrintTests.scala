package spells

class DebugPrintTests extends UnitTestConfiguration {
  test("""stackTraced(sample)(Blue) should be(styled(s"$sample at ${currentLineStackTraceElement(increaseStackTraceDepthBy = -3)}")(Blue))""") {
    forEvery(TestSamples.samples)(assert)
  }

  private def assert(sample: String) =
    stackTraced(sample)(Blue) should be(styled(s"$sample at ${currentLineStackTraceElement(increaseStackTraceDepthBy = -3)}")(Blue))

  test("It should be possible to adjust the stack depth for debugPrintln") {
    val input = "input"
    staged(input) should be(stackTraced(input))
  }

  def staged(input: String) = stackTraced(input)(increaseStackTraceDepthBy = +1)
}

trait ClipboardEnvironment extends UnitTestConfiguration with DebugPrint {
  protected var wasWriteToClipboardCalled = false

  protected lazy val fileName: String =
    currentLineStackTraceElement(increaseStackTraceDepthBy = -3).getFileName
}

class DebugPrintCopyToClipboardTests1 extends ClipboardEnvironment {
  override val isCopyFilePathToClipboardWhenDebugPrintingFeatureEnabled = true

  override val clipboardWriter: String => Unit = input => {
    wasWriteToClipboardCalled = true
    input should include(fileName)
  }

  test("If copying feature is enabled the clipboard writer should be called with the file path") {
    stackTraced("input")
    wasWriteToClipboardCalled should be(true)
  }
}

class DebugPrintCopyToClipboardTests2 extends ClipboardEnvironment {
  override val isCopyFilePathToClipboardWhenDebugPrintingFeatureEnabled = false

  override val clipboardWriter: String => Unit = input => {
    wasWriteToClipboardCalled = true
  }

  test("If copying feature is disabled the clipboard writer should not be called with the file path") {
    stackTraced("input")
    wasWriteToClipboardCalled should be(false)
  }
}

class DebugPrintCopyToClipboardTests3 extends ClipboardEnvironment {
  override val isCopyFilePathToClipboardWhenDebugPrintingFeatureEnabled = true

  override val clipboardWriter: String => Unit = input => {
    super.clipboardWriter(input)
    wasWriteToClipboardCalled = true
  }

  test("If copying feature is enabled the clipboard writer should be called with the file path") {
    isolated {
      stackTraced("input")
      wasWriteToClipboardCalled should be(true)
    }
  }

  private def isolated(code: => Unit): Unit = {
    val currentClipboardContent = Clipboard.readString()
    code
    currentClipboardContent foreach Clipboard.writeString
  }
}
