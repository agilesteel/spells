package spells

import java.text.DateFormat
import java.util.Date

trait DateOps {
  implicit class DateOpsFromSpells(value: Date) extends CustomRendering {
    def rendered: String =
      DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(value)
  }
}
