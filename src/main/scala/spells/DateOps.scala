package spells

import java.text.SimpleDateFormat
import java.util.Date

trait DateOps {
  implicit class DateOpsFromSpells(value: Date) extends CustomRendering {
    def rendered: String =
      new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z") format value
  }
}
