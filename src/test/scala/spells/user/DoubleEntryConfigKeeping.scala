package spells.user

import java.io.File

class DoubleEntryConfigKeeping extends spells.UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    (SpellsConfig.terminal.WidthInCharacters: Int) should be(160)

    (SpellsConfig.xray.report.display.DateTime: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Duration: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Location: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Thread: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Class: Boolean) should be(true)
    (SpellsConfig.xray.report.display.Type: Boolean) should be(true)

    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Description).value should be(Green.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.DateTime).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Duration).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Location).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Thread).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Class).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Type).value should be(Reset.value)
    locationAwarePropertyToAnsiStyle(SpellsConfig.xray.report.styles.Value).value should be(Magenta.value)
  }
}
