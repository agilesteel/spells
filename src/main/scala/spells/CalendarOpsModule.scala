package spells

trait CalendarOpsModule {
  this: DateOpsModule =>

  implicit class CalendarOpsFromSpells(value: java.util.Calendar) extends DateOpsFromSpells(value.getTime)
}
