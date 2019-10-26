package spells.user

import java.io.File

class DoubleEntryConfigKeeping extends spells.UnitTestConfiguration {
  test("The spells library should contain a reference.conf file") {
    new File(getClass.getResource("/reference.conf").getFile) should exist
  }

  test("The reference.conf file should be valid") {
    SpellsConfig.`custom-rendering`.display.ShortStackTraceElements.value should be(
      false
    )

    SpellsConfig.terminal.WidthInCharacters.value should be(160)

    SpellsConfig.terminal.display.Styles.value should be(true)

    SpellsConfig.xray.report.IgnoredContentKeys.value should be(
      List.empty[String]
    )

    SpellsConfig.xray.report.styles.Description.value.value should be(
      AnsiStyle.Green.value
    )
    SpellsConfig.xray.report.styles.DateTime.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Duration.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Location.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.HashCode.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Thread.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Class.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Type.value.value should be(
      AnsiStyle.Reset.value
    )
    SpellsConfig.xray.report.styles.Value.value.value should be(
      AnsiStyle.Magenta.value
    )
  }
}
