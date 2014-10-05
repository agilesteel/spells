package spells

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

class XrayTests extends UnitTestConfiguration {
  test("The expression inside of xray should be evaluated only once") {
    var timesEvaluated = 0
    def expression = timesEvaluated += 1

    SilentOutputStream out {
      expression.xray
    }

    timesEvaluated should be(1)
  }
}

class XrayResultRenderingTests extends UnitTestConfiguration {
  import XrayResultRenderingTests._

  test("The header should contains the string 'X-Ray'") {
    result.toString should include("X-Ray")
  }

  test("The datetime should be rendered in full format") {
    result.toString should include(s"DateTime | ${DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL) format timestamp}")
  }

  test("The duration should be readable by humans") {
    result.toString should include(s"Duration | 1 minute 2 seconds")
  }

  test("Location should be equal to the toString implementation of the StackTraceElement") {
    result.toString should include(s"Location | $stackTraceElement")
  }

  test("Scala encoded literals should be docded when the type is rendered") {
    result.toString should include(s"Type     | spells.Encoded + Whatever")
  }

  test("Simple values should be rendered in magenta") {
    result.toString should include(s"Value    | ${"encoded".magenta}")
  }

  test("Value of null should not be an issue") {
    result.copy(value = null).toString should include(s"Value    | ${"null".magenta}")
  }

  test("Rendered result should contain maximum 80 hyphens") {
    /*assert that*/ forEvery(result.toString split "\n") { line =>
      line.size should be <= 80
    }

    val largeResult = result.copy(value = ("V" * 100))
    val largeLines = largeResult.toString split "\n"
    val hyphenLines = largeLines.filter(line => line.filter(_ != '\r').forall(_ == '-'))

    forEvery(hyphenLines) { hyphenLine =>
      hyphenLine.filter(_ != '\r') should have size 80
    }
  }
}

object XrayResultRenderingTests {
  import Xray.Result

  private lazy val result = Result(value, duration, stackTraceElement, timestamp)
  private lazy val value = new `Encoded + Whatever`
  private lazy val duration = 62.seconds
  private lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
  private lazy val lineNumber = 4711
  private lazy val timestamp = new java.util.Date
}
