package spells

trait CalendarOpsModule {
  this: DateOpsModule with CustomRenderingModule =>

  implicit final class CalendarOpsFromSpells(value: java.util.Calendar) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      new java.text.SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format value.getTime
  }
}
