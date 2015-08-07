package spells

trait CustomRendering {
  implicit def rendered: String
}

object CustomRendering {
  private[spells] object Default {
    implicit def apply[T](input: T): Default[T] = new Default(input)
  }

  class Default[T](input: T) extends CustomRendering {
    def rendered: String = String valueOf input
  }
}
