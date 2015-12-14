package spells

/** Miscellaneous utility methods, which were too small for a distinctive module. */
trait MiscModule {
  /** This method is similar to the `identity` method from the Scala standard library, but this one does not return anything.
    * @tparam T
    * @return ()
    */
  final def noop[T]: T => Unit = _ => ()
}
