package spells

trait DateOps {
  implicit class DateOpsFromSpells(value: java.util.Date) extends CustomRendering {
    override final def rendered: String =
      new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z") format value
  }
}
