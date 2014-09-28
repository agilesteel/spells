package spells

class XRayTests extends UnitTestConfiguration {
  test("The expression inside of xray should be evaluated only once") {
    var timesEvaluated = 0
    def expression = timesEvaluated += 1

    SilentOutputStream out {
      expression.xray
    }

    timesEvaluated should be(1)
  }
}
