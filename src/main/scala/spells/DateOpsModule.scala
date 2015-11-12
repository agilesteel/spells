package spells

trait DateOpsModule {
  this: CustomRenderingModule =>

  implicit class DateOpsFromSpells(value: java.util.Date) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      new java.text.SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format value
  }

  object DateOpsFromSpells {
    object Defaults {
      val Format: String = "EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z"
    }
  }
}
