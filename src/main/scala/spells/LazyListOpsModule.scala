package spells

/** Provides utilities for `LazyList`s */
trait LazyListOpsModule {
  import LazyListOpsModule._

  implicit def byNameToFunction0Wrapper[E](byName: => E): Function0Wrapper[E] =
    new Function0Wrapper(() => byName)

  /** Creates a `LazyList` consisting of given elements.
    * In contrast to the LazyList factory from the standard library each element is evaluated on demand.
    */
  def MakeLazyList[E](elements: Function0Wrapper[E]*): LazyList[E] =
    scala.collection.immutable
      .LazyList[Function0Wrapper[E]](elements: _*)
      .map(_.function0())
}

object LazyListOpsModule {
  final class Function0Wrapper[+E](val function0: () => E) extends AnyVal
}
