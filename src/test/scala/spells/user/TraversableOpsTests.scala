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

  test("This is how the traversables should rendered") {
    val actual = Seq("I", "II", "III").rendered

    // format: OFF
    val expected =
      "scala.collection.immutable.::[java.lang.String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 | I" + "\n" +
      "1 | II" + "\n" +
      "2 | III"
    // format: ON

    actual should be(expected)
  }

  test("This is how maps should rendered") {
    val actual = Map(1 -> "I", 2 -> "II", 3 -> "III").rendered

    // format: OFF
    val expected =
      "scala.collection.immutable.Map$Map3[scala.Tuple2[Int, java.lang.String]] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 | 1 -> I" + "\n" +
      "1 | 2 -> II" + "\n" +
      "2 | 3 -> III"
    // format: ON

    actual should be(expected)
  }

  test("Recursive rendering") {
    val now = java.util.Calendar.getInstance
    val actual = List(now).rendered

    // format: OFF
    val expected =
      "scala.collection.immutable.::[java.util.Calendar] with 1 element:" + "\n" +
      "" + "\n" +
      "0 | " + now.rendered
    // format: ON

    actual should be(expected)
  }
}
