package spells.user

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

class XrayTests extends UnitTestConfiguration {
  test("The expression inside of xray should be evaluated only once") {
    var timesEvaluated = 0
    def expression() = timesEvaluated += 1

    SilentOutputStream out {
      expression.xray
    }

    timesEvaluated should be(1)
  }

  test("Monitor should always be called for .xray") {
    new MonitoringEnvironement {
      input.xray
      wasMonitorCalled should be(true)
    }
  }

  test("Monitor should only be called for .xrayIf if condition is true") {
    new MonitoringEnvironement {
      val condition = true

      input.xrayIf(condition)
      wasMonitorCalled should be(condition)
    }

    new MonitoringEnvironement {
      val condition = false

      input.xrayIf(condition)
      wasMonitorCalled should be(condition)
    }
  }

  test("Monitor should only be called for .xrayIfResult if condition is true") {
    new MonitoringEnvironement {
      val condition: XrayResult[Int] => Boolean = _.value == input

      input.xrayIfResult(condition)
      wasMonitorCalled should be(condition(XrayResult(value = input, null, null, null, null, null, null)))
    }

    new MonitoringEnvironement {
      val condition: XrayResult[Int] => Boolean = _.value != input

      input.xrayIfResult(condition)
      wasMonitorCalled should be(condition(XrayResult(value = input, null, null, null, null, null, null)))
    }
  }

  test("""Stacktrace test""") {
    forAll(TestSamples.samples)(assert)
  }

  private def assert(sample: String) =
    xrayed(sample).stackTraceElement should be(currentLineStackTraceElement(increaseStackTraceDepthBy = -3))

  test("""xrayed("").toString should startWith(color.value)""") {
    implicit val description = "description"
    val color = Yellow

    xrayed("", style = color).toString should startWith(color.value)
  }
}

trait MonitoringEnvironement {
  val input = 4711
  var wasMonitorCalled = false
  implicit val monitor: XrayResult[Any] => Unit = _ => wasMonitorCalled = true
}

class XrayResultRenderingTests extends UnitTestConfiguration {
  import XrayResultRenderingTests._

  test("The header should contains the string 'X-Ray' if description is empty") {
    result.copy(description = "").toString should include("X-Ray")
  }

  test(s"The header should contain the string '$description' if description is nonempty") {
    result.toString should include(description)
  }

  test("Should the description contain styles the header should be centered properly") {
    val descriptionValue = "desc"

    def headerWithDescription(newDescription: String): String = result.copy(description = newDescription).toString.split("\n").tail.head

    val headerWithoutStyle = headerWithDescription(descriptionValue)
    val headerWithStyle = headerWithDescription(descriptionValue.yellow)

    def sizeOfLeftPadding(header: String): Int = header.takeWhile(_ != descriptionValue.head).filter(_ == ' ').size

    sizeOfLeftPadding(headerWithoutStyle) should be(sizeOfLeftPadding(headerWithStyle))
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

  test("If type and class are equal the class should not be rendered") {
    result.toString should not include s"Class    | spells.Encoded + Whatever"
  }

  test("If type and class are different they should both be rendered") {
    val typedResult = result.copy(value = List(1, 2, 3)).toString

    typedResult should include(s"Class    | scala.collection.immutable.::")
    typedResult should include(s"Type     | scala.collection.immutable.List[Int]")
  }

  test("Simple values should be rendered in magenta") {
    result.toString should include(s"Value    | ${"encoded".magenta}")
  }

  test("""If value conains \n it should be rendered on the new line""") {
    val newResult = result.copy(value = "first\nsecond")
    newResult.toString should include(s"Value    | ${"first".magenta}")
    newResult.toString should include(s"         | ${"second".magenta}")
  }

  test("Simple values should be deeply rendered in magenta") {
    val withStyle = s"enc${"o".green}ded"
    result.copy(value = withStyle).toString should include(s"Value    | ${styled(withStyle)(Magenta)}")
  }

  test("Value of null should not be an issue") {
    result.copy(value = null).toString should include(s"Value    | ${"null".magenta}")
  }

  test("Rendered result should contain maximum 80 hyphens") {
    val max: Int = spells.terminal.`width-in-characters`

    forAll(result.toString split "\n") { line =>
      line.size should be <= max
    }

    val largeResult = result.copy(value = ("V" * (max + 20)))
    val largeLines = largeResult.toString split "\n"
    val hyphenLines = largeLines.filter(_.forall(_ == '-'))

    forAll(hyphenLines) { hyphenLine =>
      hyphenLine should have size max
    }
  }
}

class TableRenderingTests extends UnitTestConfiguration {
  test("If values are of the same length all lines should have equal lines") {
    val renderedTable = renderTable(Seq("I" -> "foo", "II" -> "bar"))
    val sizes = renderedTable.map(_.size)
    sizes.forall(_ == sizes.head) should be(true)
  }
}

object XrayResultRenderingTests {
  private lazy val result = XrayResult(
    value = value,
    duration = duration,
    stackTraceElement = stackTraceElement,
    timestamp = timestamp,
    description = description,
    thread = Thread.currentThread)

  private lazy val value = new `Encoded + Whatever`
  private lazy val duration = 62.seconds
  private lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
  private lazy val lineNumber = 4711
  private lazy val timestamp = new java.util.Date
  private lazy val description = "description"
}
