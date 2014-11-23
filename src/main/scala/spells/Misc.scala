package spells

trait Misc {
  final def noop[T]: T => Unit = _ => {}

  private[spells] def swallowException(code: => Unit): Unit =
    try code catch {
      case _: Exception => // spells should never throw exceptions
    }
}
