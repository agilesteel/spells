package spells

trait CustomRendering {
  implicit def rendered: String
}

object CustomRendering extends CalendarOps with DateOps {
  class Default(input: Any) extends CustomRendering {
    def rendered: String = String valueOf input
  }
}
