package spells.user

import java.text.SimpleDateFormat
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

  test("Monitor should always be called for xray") {
    new MonitoringEnvironement {
      input.xray
      wasMonitorCalled should be(true)
    }
  }

  test("Monitor should only be called for xrayIf if the condition yields true") {
    new MonitoringEnvironement {
      val condition: spells.Xray.Report[Int] => Boolean = _ => true

      input.xrayIf(condition)
      wasMonitorCalled should be(true)
    }
  }

  test("Monitor should not be called for xrayIf if the condition yields false") {
    new MonitoringEnvironement {
      val condition: spells.Xray.Report[Int] => Boolean = _ => false

      input.xrayIf(condition)
      wasMonitorCalled should be(false)
    }
  }

  test("""Stacktrace test""") {
    forAll(TestSamples.samples)(assert)
  }

  private def assert(sample: String) =
    xrayed(sample).stackTraceElement should be(currentLineStackTraceElement(increaseStackTraceDepthBy = -3)) // dodgy

  test("""It should be possible to implicitly pass in styles to xrayed""") {
    implicit val color = Yellow

    xrayed(10).rendered should startWith(color.value)
  }

  test("""It should be possible to implicitly pass in styles to xray""") {
    implicit val color = Yellow

    SilentOutputStream out {
      implicit def monitor(report: spells.Xray.Report[Any]): Unit =
        report.rendered should startWith(color.value)

      10.xray
    }
  }

  test("It should not be possible to implicitly pass in descriptions of type String to xrayed") {
    implicit val description: String = "description"

    xrayed("value").description should be(spells.Xray.Defaults.Description.toString)
  }

  test("It should not be possible to implicitly pass in descriptions of type String to xrat") {
    implicit val description: String = "description"

    SilentOutputStream out {
      implicit def monitor(report: spells.Xray.Report[Any]): Unit =
        report.description should be(spells.Xray.Defaults.Description.toString)

      "value".xray
    }
  }

  test("It should be possible to explicitly pass in descriptions of type String to xrayed") {
    val description: String = "description"

    xrayed("value", description).description should be(description)
  }

  test("It should be possible to explicitly pass in descriptions of type String to xray") {
    val description: String = "description"

    SilentOutputStream out {
      implicit def monitor(report: spells.Xray.Report[Any]): Unit =
        report.description should be(description)

      "value".xray(description)
    }
  }

  test("It should be possible to mix explicitly specified descriptions with implicitly specified styles for xrayed") {
    implicit val color = Cyan
    val description: String = "description"

    xrayed("value", description).description should be(description)
  }

  test("It should be possible to mix explicitly specified descriptions with implicitly specified styles for xray") {
    implicit val color = Cyan
    val description: String = "description"

    SilentOutputStream out {
      implicit def monitor(report: spells.Xray.Report[Any]): Unit = {
        report.description should be(description)
        report.rendered should startWith(color.value)
      }

      "value".xray(description)
    }
  }
}

trait MonitoringEnvironement {
  val input = 4711
  var wasMonitorCalled = false
  implicit def monitor[T](report: spells.Xray.Report[T]): Unit = wasMonitorCalled = true
}

class XrayReportRenderingTests extends UnitTestConfiguration {
  import XrayReportRenderingTests._

  test("The header should contain the string 'X-Ray' if description is empty") {
    createReport(description = "").rendered should include("X-Ray".green)
  }

  test("Empty styled header should result in the default one") {
    createReport(description = "".yellow).rendered should include("X-Ray".green)
  }

  test("X-Ray header should still be rendered green") {
    createReport(description = "X-Ray").rendered should include("X-Ray".green)
  }

  test(s"The header should contain the string '$description' if description is nonempty") {
    createReport().rendered should include(description)
  }

  test("If the description contains styles the header should still be centered properly") {
    val descriptionValue = "desc"

    def headerWithDescription(newDescription: String): String =
      createReport(description = newDescription).rendered.split("\n").tail.head

    val headerWithoutStyle = headerWithDescription(descriptionValue)
    val headerWithStyle = headerWithDescription(descriptionValue.yellow)

    def sizeOfLeftPadding(header: String): Int = header.takeWhile(_ != descriptionValue.head).filter(_ == ' ').size

    sizeOfLeftPadding(headerWithoutStyle) should be(sizeOfLeftPadding(headerWithStyle))
  }

  test("The datetime should be rendered in full format") {
    createReport().rendered should include(s"""DateTime | ${new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z") format timestamp.getTime}""")
  }

  test("The duration should be readable by humans") {
    createReport().rendered should include(s"Duration | 1 minute 2 seconds")
  }

  test("Location should be equal to the toString implementation of the StackTraceElement") {
    createReport().rendered should include(s"Location | $stackTraceElement")
  }

  test("If type and class are equal the class should not be rendered") {
    createReport().rendered should not include s"Class    | spells.Encoded + Whatever"
  }

  test("If type and class are different they should both be rendered") {
    val typedReport = createReport(value = List(1, 2, 3)).rendered

    typedReport should include(s"Class    | scala.collection.immutable.::")
    typedReport should include(s"Type     | scala.collection.immutable.List[Int]")
  }

  test("Simple values should be rendered in magenta") {
    createReport().rendered should include(s"Value    | ${"encoded".magenta}")
  }

  test("""If value conains '\n' it should be rendered on the new line""") {
    val newReportString = createReport(value = "first\nsecond").rendered

    newReportString should include(s"Value    | ${"first".magenta}")
    newReportString should include(s"         | ${"second".magenta}")
  }

  test("Simple values should be deeply (other styles should not be lost) rendered in magenta") {
    val withStyle = s"enc${"o".green}ded"
    val reportWithStyle = createReport(value = withStyle)

    reportWithStyle.rendered should include(s"Value    | ${styled(withStyle)(Magenta)}")
  }

  test("Nulls should be rendered as values and thus not throw exceptions") {
    createReport(value = null).rendered should include(s"Value    | ${"null".magenta}")
  }

  test(s"Rendered report should contain maximum ${spells.terminal.`width-in-characters`} hyphens") {
    val maxWidthInCharacters: Int = spells.terminal.`width-in-characters`

    forAll(createReport().rendered split "\n") { line =>
      line.size should be <= maxWidthInCharacters
    }

    val largeReport = createReport(value = ("V" * (maxWidthInCharacters + 20)))
    val largeLines = largeReport.rendered split "\n"
    val hyphenLines = largeLines.filter(_.forall(_ == '-'))

    forAll(hyphenLines) { hyphenLine =>
      hyphenLine should have size maxWidthInCharacters
    }
  }

  test("Custom rendering for java.util.Calendar") {
    val now = Calendar.getInstance

    assume(now != timestamp)

    xrayed(now).rendered should include {
      new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss.SSS z Z").format(now.getTime).magenta
    }
  }

  test("Multiline rendering should not cause any style loss") {
    // format: OFF
    val expected =
      "Value    | " + "scala.collection.immutable.::[java.lang.String] with 3 elements:".magenta + "\n" +
      "         | " + "" + "\n" +
      "         | " + "0 | I".magenta + "\n" +
      "         | " + "1 | II".magenta + "\n" +
      "         | " + "2 | III".magenta
    // format: ON

    SilentOutputStream out {
      implicit def monitor(report: spells.Xray.Report[Any]): Unit = {
        report.rendered should include(expected)
      }

      Seq("I", "II", "III").xray
    }
  }

  test("The customRenderedTableForXray helper method should yield an empty Seq when given an empty input") {
    spells.Xray.Report.customRenderedTableForXray(_ => Seq.empty, styles = Map.empty, availableWidthInCharacters = util.Random.nextInt) should be(Vector.empty[String])
  }
}

object XrayReportRenderingTests {
  private def createReport[T: Manifest](
    value: T = value,
    duration: Duration = duration,
    stackTraceElement: StackTraceElement = stackTraceElement,
    timestamp: Calendar = timestamp,
    description: String = description,
    style: spells.Ansi.Style = Reset,
    rendering: T => spells.CustomRendering = spells.CustomRendering.Defaults.Any): spells.Xray.Report[T] =
    new spells.Xray.Report[T](
      value = value,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = description,
      thread = Thread.currentThread,
      rendering = rendering
    )

  private lazy val value = new `Encoded + Whatever`
  private lazy val duration = 62.seconds
  private lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
  private lazy val lineNumber = 4711
  private lazy val timestamp = Calendar.getInstance
  private lazy val description = "description"
}
