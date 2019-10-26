package spells.user

class ClipboardTests extends spells.UnitTestConfiguration {
  test("Clipboard CRUD - (Clipboard.writeString and Clipboard.readString)") {
    isolated {
      Clipboard.writeString("test")
      Clipboard.readString should be(scala.util.Success("test"))
    }
  }

  private def isolated(code: => Unit): Unit = {
    val currentClipboardContent = Clipboard.readString
    try code
    finally currentClipboardContent foreach Clipboard.writeString
  }
}
