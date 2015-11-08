package spells

trait CalendarOpsModule {
  this: DateOps =>

  implicit class CalendarOpsFromSpells(value: java.util.Calendar) extends DateOpsFromSpells(value.getTime)
}
