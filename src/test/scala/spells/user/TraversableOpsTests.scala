package spells.user

class TraversableOpsTests extends UnitTestConfiguration {
  test("Empty traversables should be rendered the same way as toString") {
    Traversable.empty[Int].rendered should be(Traversable.empty[String].toString)
  }

  test("A traversable with 1 element should not contain the word 'elements'") {
    Traversable(1).rendered should not include "elements"
  }

  test("A traversable with 1 element should contain the word 'element'") {
    Traversable(1).rendered should include("element")
  }

  test("A traversable with 1 element should contain the type") {
    Traversable(1).rendered should include("[Int]")
    Traversable("").rendered should include("[java.lang.String]")
  }

  test("A traversable heaeder should contain the class of the traversable parameterised with the type of its elements as well the number of elements") {
    Traversable(1).rendered should include("scala.collection.immutable.::[Int] with 1 element:\n\n")
  }
}
