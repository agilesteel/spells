package spells

trait DateOpsModule {
  implicit class DateOpsFromSpells(value: java.util.Date) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z") format value
  }
}
