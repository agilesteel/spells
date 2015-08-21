package spells.user

class Tuple2OpsTests extends UnitTestConfiguration {
  test("Empty traversables should be rendered the same way as toString") {
    (1 -> "II").rendered should be("1 -> II")
  }

  test("Recursive rendering") {
    val now = java.util.Calendar.getInstance
    (now -> now).rendered should be(s"${now.rendered} -> ${now.rendered}")
  }
}
