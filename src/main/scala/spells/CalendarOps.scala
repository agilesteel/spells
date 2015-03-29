package spells

import java.util.Calendar

trait CalendarOps {
  this: DateOps =>

  implicit class CalendarOpsFromSpells(value: Calendar) extends DateOpsFromSpells(value.getTime)
}
