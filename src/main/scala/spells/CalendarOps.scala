package spells

trait CalendarOps {
  this: DateOps =>

  implicit class CalendarOpsFromSpells(value: java.util.Calendar) extends DateOpsFromSpells(value.getTime)
}
