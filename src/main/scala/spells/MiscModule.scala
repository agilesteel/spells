package spells

trait MiscModule {
  final def noop[T]: T => Unit = _ => ()
}
