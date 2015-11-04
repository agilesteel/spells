package spells.user

class DoubleEntryConfigKeeping extends UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new java.io.File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    (spells.terminal.WidthInCharacters: Int) should be(160)

    (spells.xray.report.display.DateTime: Boolean) should be(true)
    (spells.xray.report.display.Duration: Boolean) should be(true)
    (spells.xray.report.display.Location: Boolean) should be(true)
    (spells.xray.report.display.Thread: Boolean) should be(true)
    (spells.xray.report.display.Class: Boolean) should be(true)
    (spells.xray.report.display.Type: Boolean) should be(true)

    (spells.xray.report.styles.Description: spells.Ansi.Style) should be(Green)
    (spells.xray.report.styles.DateTime: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Duration: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Location: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Thread: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Class: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Type: spells.Ansi.Style) should be(Reset)
    (spells.xray.report.styles.Value: spells.Ansi.Style) should be(Magenta)
  }
}
