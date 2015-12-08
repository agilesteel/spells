package spells

/** Provides utility methods for java.util.Calendar. */
trait CalendarOpsModule {
  this: DateOpsModule with CustomRenderingModule =>

  import java.text.SimpleDateFormat
  import java.util.Calendar

  implicit final class CalendarOpsFromSpells(value: Calendar) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      new SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format value.getTime
  }
}
