package spells

/** Provides utility methods for java.util.Calendar. */
trait CalendarOpsModule {
  this: CustomRenderingModule with DateOpsModule with StringOpsModule with SpellsConfigModule =>

  import java.text.SimpleDateFormat
  import java.util.Calendar

  implicit final class CalendarOpsFromSpells(value: Calendar) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
      new SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format value.getTime
  }
}
