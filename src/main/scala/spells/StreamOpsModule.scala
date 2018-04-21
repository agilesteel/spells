package spells

/** Provides utilities for `Stream`s */
trait StreamOpsModule {
  import StreamOpsModule._

  implicit def byNameToFunction0Wrapper[E](byName: => E): Function0Wrapper[E] =
    new Function0Wrapper(() => byName)

  /** Creates a stream consisting of given elements.
    * In contrast to the Stream factory from the standard library each element is evaluated on demand.
    */
  def MakeStream[E](elements: Function0Wrapper[E]*): Stream[E] =
    scala.collection.immutable.Stream[Function0Wrapper[E]](elements: _*).map(_.function0())
}

object StreamOpsModule {
  final class Function0Wrapper[+E](val function0: () => E) extends AnyVal
}
