package spells.user

class StreamOpsTests extends spells.UnitTestConfiguration {
  test("The arguments to the Stream factory from spells should be evaluated only on demand") {
    var is2Evaluated = false

    lazy val `1` = 1
    lazy val `2` = {
      is2Evaluated = true

      2
    }

    MakeStream(`1`, `2`)

    is2Evaluated should be(false)
  }

  test("The Stream produced by the factory from spells should equal the Stream produced by the standard library") {
    MakeStream(1, 2) should be(scala.collection.immutable.Stream(1, 2))
  }

  test("The Stream factory from spells should not shadow Stream's companion object") {
    Stream.Empty should be(scala.collection.immutable.Stream.Empty)
  }

  test("Values should only be implicitly converted to StreamOpsModule.Function0Wrapper but not to Function0") {
    def good[E](argument: spells.StreamOpsModule.Function0Wrapper[E]): Any = argument
    good(1)

    def bad[E](argument: Function0[E]): Any = argument
    "bad(1)" shouldNot typeCheck
  }
}
