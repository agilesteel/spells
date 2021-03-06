package spells.user

import scala.reflect.runtime.universe._

class IterableOpsTests extends spells.UnitTestConfiguration {
  test("Empty iterables should be rendered the same way as toString") {
    Iterable.empty[Int].rendered should be(
      Iterable.empty[String].toString
    )
  }

  test(
    "Empty arrays should be rendered the same way as empty scala collections"
  ) {
    Array.empty[Int].rendered should be("Array()")
  }

  test(
    "Empty java collections should be rendered the same way as scala collections, but prefixed with a full class name"
  ) {
    new java.util.HashSet[String].rendered should be("java.util.HashSet()")
  }

  test(
    "Empty java maps should be rendered the same way as scala collections, but prefixed with a full class name"
  ) {
    new java.util.HashMap[String, Int].rendered should be("java.util.HashMap()")
  }

  test("A iterable with 1 element should contain the word 'element'") {
    Iterable(1).rendered should include("element")
  }

  test("A iterable with 1 element should not contain the word 'elements'") {
    Iterable(1).rendered should not include "elements"
  }

  test(
    "A iterable with multiple elements should contain the word 'elements'"
  ) {
    Iterable(1, 2).rendered should include("elements")
  }

  test("A iterable with 1 element should contain the type") {
    Iterable(1).rendered should include regex "[.*Int]"
    Iterable("").rendered should include regex "[.*String]"
  }

  test(
    "A iterable header should contain the class of the iterable parameterised with the type of its elements as well the number of elements the iterable contains"
  ) {
    val tag = typeTag[Int]
    Iterable(1).rendered should include(
      s"Iterable[${tag.tpe}] with 1 element:\n\n"
    )
  }

  test(
    "An array header should contain the class word Array parameterised with the type of its elements as well the number of elements the iterable contains"
  ) {
    val tag = typeTag[Int]
    Array(1).rendered should include(s"Array[${tag.tpe}] with 1 element:\n\n")
  }

  test("This is how the iterables should be rendered") {
    val actual = Seq("I", "II", "III").rendered

    // format: OFF
    val firstPart =
      "Seq["

    val secondPart =
      "String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 │ I" + "\n" +
      "1 │ II" + "\n" +
      "2 │ III"
    // format: ON

    actual should include(firstPart)
    actual should include(secondPart)
  }

  test("This is how recursive renderng for iterables should work") {
    val inner = Seq("I", "II", "III")
    val actual = List(inner, inner, inner).rendered

    // partially tested since regexes don't like weird unicode characters
    actual should include("2 │ Seq[")
    actual should include("  │ 0 │ I" + "\n")
    actual should include("  │ 1 │ II" + "\n")
    actual should include("  │ 2 │ III" + "\n")
  }

  test(
    "Recursive renderng for iterables should include recursive line wrapping"
  ) {
    val availableWidthInCharacters =
      SpellsConfig.terminal.WidthInCharacters.value
    val sizeOfKeyWithSeparator = 8

    def atom(c: Char): String =
      c.toString * (availableWidthInCharacters - sizeOfKeyWithSeparator)

    val xs = atom('x')
    val ys = atom('y')
    val zs = atom('z')

    val inner = Seq(xs + " " + xs, ys + " " + ys, zs + " " + zs)
    val actual = List(inner, inner, inner).rendered

    // partially tested since regexes don't like weird unicode characters
    actual should include("2 │ Seq[")
    actual should include("  │ 0 │ " + xs + "\n")
    actual should include("  │   │ " + xs + "\n")
    actual should include("  │ 1 │ " + ys + "\n")
    actual should include("  │   │ " + ys + "\n")
    actual should include("  │ 2 │ " + zs + "\n")
    actual should include("  │   │ " + zs)
  }

  test("This is how maps should be rendered") {
    val actual = Map(1 -> "I", 2 -> "II", 3 -> "III").rendered

    // format: OFF
    val firstPart =
      "scala.collection.immutable.Map["

    val secondPart =
      "String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 │ 1 -> I" + "\n" +
      "1 │ 2 -> II" + "\n" +
      "2 │ 3 -> III"
    // format: ON

    actual should include(firstPart)
    actual should include(secondPart)
  }

  test("Recursive rendering should work out of the box") {
    val now = java.util.Calendar.getInstance
    val actual = List(now).rendered

    // format: OFF
    val expected =
      "List[java.util.Calendar] with 1 element:" + "\n" +
      "" + "\n" +
      "0 │ " + now.rendered
    // format: ON

    actual should be(expected)
  }

  test("This is how java collections should be rendered") {
    val actual = {
      val collection = new java.util.ArrayList[String]
      collection add "I"
      collection add "II"
      collection add "III"

      collection.rendered
    }

    // format: OFF
    val expected =
      "java.util.ArrayList[String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 │ I" + "\n" +
      "1 │ II" + "\n" +
      "2 │ III"
    // format: ON

    actual should be(expected)
  }

  test("This is how java maps should be rendered") {
    val actual = {
      val collection = new java.util.HashMap[Int, String]
      collection.put(1, "I")
      collection.put(2, "II")
      collection.put(3, "III")

      collection.rendered
    }

    // format: OFF
    val firstPart =
      "java.util.HashMap["

    val secondPart =
      "String] with 3 elements:" + "\n" +
      "" + "\n" +
      "0 │ 1 -> I" + "\n" +
      "1 │ 2 -> II" + "\n" +
      "2 │ 3 -> III"
    // format: ON

    actual should include(firstPart)
    actual should include(secondPart)
  }

  test("If values are of the same length all lines should have equal size") {
    val table = renderedTable(
      _ => Seq("I" -> "foo", "II" -> "bar"),
      availableWidthInCharacters = Int.MaxValue
    )
    val sizes = table.map(_.size)
    sizes.forall(_ == sizes.head) should be(true)
  }

  test("Lines should be wrapped") {
    val availableWidthInCharacters =
      SpellsConfig.terminal.WidthInCharacters.value
    val sizeOfKeyWithSeparator = 4
    def atom(c: Char) =
      c.toString * (availableWidthInCharacters - sizeOfKeyWithSeparator)
    val toBeSpliced = atom('x') + " " + atom('y')
    val table =
      renderedTable(_ => Seq("I" -> toBeSpliced), availableWidthInCharacters)

    table should be {
      Vector(
        // format: OFF
        "I │ " + atom('x') + "\n" +
        "  │ " + atom('y')
      // format: ON
      )
    }
  }

  test(
    "The renderedTable helper method should yield an empty Seq when given an empty input"
  ) {
    renderedTable(
      _ => Seq.empty,
      availableWidthInCharacters = util.Random.nextInt()
    ) should be(Vector.empty[String])
  }
}
