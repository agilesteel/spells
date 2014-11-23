package spells

class ClipboardTests extends UnitTestConfiguration {
  test("Clipboard CRUD") {
    isolated {
      Clipboard.writeString("test")
      Clipboard.readString should be(scala.util.Success("test"))
    }
  }

  private def isolated(code: => Unit): Unit = {
    val currentClipboardContent = Clipboard.readString
    try code finally currentClipboardContent foreach Clipboard.writeString
  }
}
