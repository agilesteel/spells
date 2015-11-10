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

    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Description) should be(Green)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.DateTime) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Duration) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Location) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Thread) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Class) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Type) should be(Reset)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Value) should be(Magenta)
  }
}
