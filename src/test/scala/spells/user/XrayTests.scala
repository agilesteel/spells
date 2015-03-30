package spells.user

import java.text.DateFormat
import java.util.Calendar
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

  test("The header should contain the string 'X-Ray' if description is empty") {
    XrayResult(
      value = value,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = "",
      thread = Thread.currentThread
    ).toString should include("X-Ray".green)
  }

  test("Styles only header should result in the default one") {
    XrayResult(
      value = value,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = "".yellow,
      thread = Thread.currentThread
    ).toString should include("X-Ray".green)
  }

  test("X-Ray header should still be rendered green") {
    XrayResult(
      value = value,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = "X-Ray",
      thread = Thread.currentThread
    ).toString should include("X-Ray".green)
  }

  test(s"The header should contain the string '$description' if description is nonempty") {
    result.toString should include(description)
  }

  test("Should the description contain styles the header should be centered properly") {
    val descriptionValue = "desc"

    def headerWithDescription(newDescription: String): String =
      XrayResult(
        value = value,
        duration = duration,
        stackTraceElement = stackTraceElement,
        timestamp = timestamp,
        description = newDescription,
        thread = Thread.currentThread
      ).toString.split("\n").tail.head

    val headerWithoutStyle = headerWithDescription(descriptionValue)
    val headerWithStyle = headerWithDescription(descriptionValue.yellow)

    def sizeOfLeftPadding(header: String): Int = header.takeWhile(_ != descriptionValue.head).filter(_ == ' ').size

    sizeOfLeftPadding(headerWithoutStyle) should be(sizeOfLeftPadding(headerWithStyle))
  }

  test("The datetime should be rendered in full format") {
    result.toString should include(s"DateTime | ${DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL) format timestamp.getTime}")
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
    val typedResult =
      XrayResult(
        value = List(1, 2, 3),
        duration = duration,
        stackTraceElement = stackTraceElement,
        timestamp = timestamp,
        description = description,
        thread = Thread.currentThread
      ).toString

    typedResult should include(s"Class    | scala.collection.immutable.::")
    typedResult should include(s"Type     | scala.collection.immutable.List[Int]")
  }

  test("Simple values should be rendered in magenta") {
    result.toString should include(s"Value    | ${"encoded".magenta}")
  }

  test("""If value conains \n it should be rendered on the new line""") {
    val newResultString =
      XrayResult(
        value = "first\nsecond",
        duration = duration,
        stackTraceElement = stackTraceElement,
        timestamp = timestamp,
        description = description,
        thread = Thread.currentThread
      ).toString

    newResultString should include(s"Value    | ${"first".magenta}")
    newResultString should include(s"         | ${"second".magenta}")
  }

  test("Simple values should be deeply rendered in magenta") {
    val withStyle = s"enc${"o".green}ded"

    val resultWithStyle =
      XrayResult(
        value = withStyle,
        duration = duration,
        stackTraceElement = stackTraceElement,
        timestamp = timestamp,
        description = description,
        thread = Thread.currentThread
      )

    resultWithStyle.toString should include(s"Value    | ${styled(withStyle)(Magenta)}")
  }

  test("Value of null should not be an issue") {
    XrayResult(
      value = null,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = description,
      thread = Thread.currentThread
    ).toString should include(s"Value    | ${"null".magenta}")
  }

  test(s"Rendered result should contain maximum ${spells.terminal.`width-in-characters`} hyphens") {
    val maxWidthInCharacters: Int = spells.terminal.`width-in-characters`

    forAll(result.toString split "\n") { line =>
      line.size should be <= maxWidthInCharacters
    }

    val largeResult =
      XrayResult(
        value = ("V" * (maxWidthInCharacters + 20)),
        duration = duration,
        stackTraceElement = stackTraceElement,
        timestamp = timestamp,
        description = description,
        thread = Thread.currentThread
      )

    val largeLines = largeResult.toString split "\n"
    val hyphenLines = largeLines.filter(_.forall(_ == '-'))

    forAll(hyphenLines) { hyphenLine =>
      hyphenLine should have size maxWidthInCharacters
    }
  }

  test("Custom rendering for Calendar") {
    val now = Calendar.getInstance

    assume(now != timestamp)

    XrayResult(
      value = now,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = description,
      thread = Thread.currentThread
    ).toString should include {
        DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(now.getTime).magenta
      }
  }
}

class TableRenderingTests extends UnitTestConfiguration {
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

  test("""Lines should be wrapped, but the style should be preserved for the "Value" key""") {
    val maxWidthInCharacters = spells.terminal.`width-in-characters`
    val sizeOfKeyWithSeparator = 8
    def atom(c: Char) = c.toString * (maxWidthInCharacters - sizeOfKeyWithSeparator)
    val toBeSpliced = atom('x') + " " + atom('y')
    val table = renderedTable(Seq("Value" -> toBeSpliced))
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
  private lazy val timestamp = Calendar.getInstance
  private lazy val description = "description"
}
