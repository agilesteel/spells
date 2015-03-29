package spells

trait CustomRendering {
  implicit def rendered: String
}

object CustomRendering extends CalendarOps with DateOps
