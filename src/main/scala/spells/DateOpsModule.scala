package spells

/** Provides utility methods for java.util.Date. */
trait DateOpsModule {
  this: CustomRenderingModule with StringOpsModule with SpellsConfigModule =>

  import java.text.SimpleDateFormat
  import java.util.Date

  final implicit class DateOpsFromSpells(value: Date) extends CustomRendering {
    final override def rendered(
        implicit
        availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String =
      new SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format value
  }

  object DateOpsFromSpells {
    object Defaults {
      val Format: String = "EEEE, MMMM d, yyyy HH:mm:ss.SSS Z z"
    }
  }
}
