package spells

/** Provides miscellaneous utility methods, which were too small for a distinctive module. */
trait MiscModule {
  import scala.concurrent.duration._

  /** This method is similar to the `identity` method from the Scala standard library, but this one does not return anything.
    * @tparam T input type
    * @return ()
    */
  final def noop[T]: T => Unit = _ => ()

  /** Measures the execution time of an expression.
    * @param expression the expression to be measured
    * @tparam T the result type of the expression
    * @return a Tuple2 with the value of the expression and it's execution time
    */
  final def measureExecutionTime[T](expression: => T): (T, Duration) = {
    val start = System.nanoTime
    val value = expression
    val duration = System.nanoTime - start

    value -> duration.nanos
  }
}
