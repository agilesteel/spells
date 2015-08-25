package spells.user

class TraversableOpsTests extends UnitTestConfiguration {
  test("Empty traversables should be rendered the same way as toString") {
    Traversable.empty[Int].rendered should be(Traversable.empty[String].toString)
  }

  test("Empty arrays should be rendered the same way as empty scala collections") {
    Array.empty[Int].rendered should be("Array()")
  }

  test("Empty java collections should be rendered the same way as scala collections, but prefixed with a full class name") {
    new java.util.HashSet[String].rendered should be("java.util.HashSet()")
  }

  test("Empty java maps should be rendered the same way as scala collections, but prefixed with a full class name") {
    new java.util.HashMap[String, Int].rendered should be("java.util.HashMap()")
  }

  test("A traversable with 1 element should contain the word 'element'") {
    Traversable(1).rendered should include("element")
  }

  test("A traversable with 1 element should not contain the word 'elements'") {
    Traversable(1).rendered should not include "elements"
  }

  test("A traversable with multiple elements should contain the word 'elements'") {
    Traversable(1, 2).rendered should include("elements")
  }

  test("A traversable with 1 element should contain the type") {
    Traversable(1).rendered should include("[Int]")
    Traversable("").rendered should include("[java.lang.String]")
  }

  test("A traversable heaeder should contain the class of the traversable parameterised with the type of its elements as well the number of elements the traversable contains") {
    Traversable(1).rendered should include("scala.collection.immutable.::[Int] with 1 element:\n\n")
  }

  test("This is how the traversables should be rendered") {
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

  test("This is how maps should be rendered") {
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

  test("Recursive rendering should work out of the box") {
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

  test("This is how java collections should be rendered should be rendered") {
    val actual = {
      val collection = new java.util.ArrayList[String]
      collection add "I"
      collection add "II"
      collection add "III"

      collection.rendered
    }

    // format: OFF
    val expected =
      "java.util.ArrayList[java.lang.String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 | I" + "\n" +
      "1 | II" + "\n" +
      "2 | III"
    // format: ON

    actual should be(expected)
  }

  test("This is how java maps should be rendered") {
    val actual = {
      val collection = new java.util.HashMap[Int, String]
      collection put (1, "I")
      collection put (2, "II")
      collection put (3, "III")

      collection.rendered
    }

    // format: OFF
    val expected =
      "java.util.HashMap[java.util.Map$Entry[Int, java.lang.String]] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 | 1 -> I" + "\n" +
      "1 | 2 -> II" + "\n" +
      "2 | 3 -> III"
    // format: ON

    actual should be(expected)
  }

  test("If values are of the same length all lines should have equal size") {
    val table = renderedTable(Seq("I" -> "foo", "II" -> "bar"))
    val sizes = table.map(_.size)
    sizes.forall(_ == sizes.head) should be(true)
  }

  test("Lines should be wrapped") {
    val maxWidthInCharacters = spells.terminal.`width-in-characters`
    val sizeOfKeyWithSeparator = 4
    def atom(c: Char) = c.toString * (maxWidthInCharacters - sizeOfKeyWithSeparator)
    val toBeSpliced = atom('x') + " " + atom('y')
    val table = renderedTable(Seq("I" -> toBeSpliced))
    table should be {
      Vector(
      // format: OFF
        "I | " + atom('x') + "\n" +
        "  | " + atom('y') + Reset.value
      // format: ON
      )
    }
  }

  test("""Lines should be wrapped, but the style should be preserved for the "Value" key, assuming custom styles are passed in""") {
    val maxWidthInCharacters = spells.terminal.`width-in-characters`
    val sizeOfKeyWithSeparator = 8
    def atom(c: Char) = c.toString * (maxWidthInCharacters - sizeOfKeyWithSeparator)
    val toBeSpliced = atom('x') + " " + atom('y')
    val table = renderedTable(Seq("Value" -> toBeSpliced), styles = Map("Value" -> Magenta) withDefaultValue Reset)
    table should be {
      Vector(
      // format: OFF
        "Value | " + atom('x').magenta + "\n" +
        "      | " + atom('y').magenta
      // format: ON
      )
    }
  }
}
