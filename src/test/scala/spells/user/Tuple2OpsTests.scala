package spells.user

class Tuple2OpsTests extends spells.UnitTestConfiguration {
  test("Tuple2 should always be rendered with an arrow") {
    (1 -> "II").rendered should be("1 -> II")
  }

  test("Recursive rendering should work out of the box") {
    val now = java.util.Calendar.getInstance
    (now -> now).rendered should be(s"${now.rendered} -> ${now.rendered}")
  }
}
