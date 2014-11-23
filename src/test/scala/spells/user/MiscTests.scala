package spells.user

class MiscTests extends UnitTestConfiguration {
  test("Coverage should be happy") {
    noop[Int](4711)
    noException should be thrownBy swallowException(throw new Exception)
  }
}
