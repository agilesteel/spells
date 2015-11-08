package spells.user

import java.text.SimpleDateFormat
import java.util.Calendar
import scala.concurrent.duration._

class XrayReportRenderingTests extends spells.UnitTestConfiguration {
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

  test(s"Rendered report should contain maximum ${SpellsConfig.terminal.WidthInCharacters} hyphens") {
    val maxWidthInCharacters: Int = SpellsConfig.terminal.WidthInCharacters

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
      implicit def monitor(report: XrayReport[Any]): Unit = {
        report.rendered should include(expected)
      }

      Seq("I", "II", "III").xray
    }
  }

  test("The customRenderedTableForXray helper method should yield an empty Seq when given an empty input") {
    XrayReport.customRenderedTableForXray(_ => Seq.empty, styles = Map.empty, availableWidthInCharacters = util.Random.nextInt) should be(Vector.empty[String])
  }
}

object XrayReportRenderingTests {
  private def createReport[T: Manifest](
    value: T = value,
    duration: Duration = duration,
    stackTraceElement: StackTraceElement = stackTraceElement,
    timestamp: Calendar = timestamp,
    description: String = description,
    style: spells.AnsiModule.Style = Reset,
    rendering: T => spells.CustomRendering = spells.CustomRendering.Defaults.Any): XrayReport[T] =
    new XrayReport[T](
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
