package spells.user

class LazyListOpsTests extends spells.UnitTestConfiguration {
  test(
    "The arguments to the LazyList factory from spells should be evaluated only on demand"
  ) {
    var is2Evaluated = false

    lazy val `1` = 1
    lazy val `2` = {
      is2Evaluated = true

      2
    }

    MakeLazyList(`1`, `2`)

    is2Evaluated should be(false)
  }

  test(
    "The LazyList produced by the factory from spells should equal the LazyList produced by the standard library"
  ) {
    MakeLazyList(1, 2) should be(scala.collection.immutable.LazyList(1, 2))
  }

  test(
    "The LazyList factory from spells should not shadow LazyList's companion object"
  ) {
    LazyList.empty should be(scala.collection.immutable.LazyList.empty)
  }

  test(
    "Values should only be implicitly converted to StreamOpsModule.Function0Wrapper but not to Function0"
  ) {
    def good[E](argument: spells.LazyListOpsModule.Function0Wrapper[E]): Any =
      argument

    good(1)

    def bad[E](argument: Function0[E]): Any =
      argument

    "bad(1)" shouldNot typeCheck
  }
}
