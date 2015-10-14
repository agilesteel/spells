package spells

trait DateOps {
  implicit class DateOpsFromSpells(value: java.util.Date) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: Int = spells.terminal.`width-in-characters`): String =
      new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z") format value
  }
}
