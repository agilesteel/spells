package spells

trait Misc {
  final def noop[T]: T => Unit = _ => ()
}
