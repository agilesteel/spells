package spells.user

import java.io.File

class DoubleEntryConfigKeeping extends spells.UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    SpellsConfig.`custom-rendering`.display.ShortStackTraceElements.value should be(false)

    SpellsConfig.terminal.WidthInCharacters.value should be(160)

    SpellsConfig.terminal.display.Styles.value should be(true)

    SpellsConfig.xray.report.display.DateTime.value should be(true)
    SpellsConfig.xray.report.display.Duration.value should be(true)
    SpellsConfig.xray.report.display.Location.value should be(true)
    SpellsConfig.xray.report.display.HashCode.value should be(true)
    SpellsConfig.xray.report.display.Thread.value should be(true)
    SpellsConfig.xray.report.display.Class.value should be(true)
    SpellsConfig.xray.report.display.Type.value should be(true)

    SpellsConfig.xray.report.styles.Description.value.value should be(Green.value)
    SpellsConfig.xray.report.styles.DateTime.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Duration.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Location.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.HashCode.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Thread.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Class.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Type.value.value should be(Reset.value)
    SpellsConfig.xray.report.styles.Value.value.value should be(Magenta.value)
  }
}
