package spells

trait Misc {
  def Noop[T]: T => Unit = _ => {}
}
