package spells.user

class DoubleEntryConfigKeeping extends spells.UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new java.io.File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    (SpellsConfig.terminal.WidthInCharacters: Int) should be(160)

    (SpellsConfig.xray.report.display.DateTime: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Duration: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Location: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Thread: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Class: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Type: Boolean) should be(true)

    (SpellsConfig.xray.report.styles.Description: spells.AnsiModule.Style) should be(Green)
    (SpellsConfig.xray.report.styles.DateTime: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Duration: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Location: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Thread: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Class: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Type: spells.AnsiModule.Style) should be(Reset)
    (SpellsConfig.xray.report.styles.Value: spells.AnsiModule.Style) should be(Magenta)
  }
}
