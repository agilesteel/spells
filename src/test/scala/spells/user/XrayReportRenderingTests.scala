package spells.user

import java.text.SimpleDateFormat
import java.util.{ Date, Calendar }
import scala.collection._
import scala.concurrent.duration._
import scala.reflect.runtime.universe._

import AnsiStyle._

class XrayReportRenderingTests extends spells.UnitTestConfiguration {
  test("The header should contain the string 'Xray' if description is empty") {
    XrayReportRenderingTests.createReport(description = "").rendered should include("Xray".green)
  }

  test("Empty styled header should result in the default one") {
    XrayReportRenderingTests.createReport(description = "".yellow).rendered should include("Xray".green)
  }

  test("Xray header should still be rendered green") {
    XrayReportRenderingTests.createReport(description = "Xray").rendered should include("Xray".green)
  }

  test("The size of the description should be involved into the size calculation of the Xray table") {
    val maxWidth = SpellsConfig.terminal.WidthInCharacters.value
    XrayReportRenderingTests.createReport(description = "x" * maxWidth).rendered should include("-" * maxWidth)
  }

  test("The size of the description should be involved into the size calculation of the Xray table, but it should not cause it to grow over its limits") {
    val maxWidth = SpellsConfig.terminal.WidthInCharacters.value
    XrayReportRenderingTests.createReport(description = "x" * maxWidth + 1).rendered should include("-" * maxWidth)
  }

  test("The description should be wrapped and centered") {
    val maxWidth = SpellsConfig.terminal.WidthInCharacters.value
    val actual = ("x" * maxWidth) + " " + "y"
    val expected = ("x" * maxWidth) + "\n" + (" " * ((maxWidth - 1) / 2)) + "y"

    XrayReportRenderingTests.createReport(description = actual).rendered should include(expected)
  }

  test("Multiline description should be centered") {
    val maxWidth = SpellsConfig.terminal.WidthInCharacters.value
    val actual = "hello\nworld"
    val report = XrayReportRenderingTests.createReport(description = actual).rendered
    val expected = {
      val tableWidth = report.split("\n").find(_.forall(_ == '-')).fold(maxWidth)(_.size)

      (" " * ((tableWidth - "hello".size) / 2)) + s"${Green.value}hello" + "\n" + (" " * ((tableWidth - "world".size) / 2)) + "world"
    }

    report should include(expected)
  }

  test("Multiline description should be centered but not padded if it happens to be the longest line") {
    val maxWidth = SpellsConfig.terminal.WidthInCharacters.value
    def createReportOnSameLine(description: String = "") = XrayReportRenderingTests.createReport(description = description)
    val report = createReportOnSameLine().rendered
    val tableWidth = report.split("\n").maxBy(_.size).size
    tableWidth should be < maxWidth
    val description = ("x" * (tableWidth + 10)) + "\n" + ("y" * (tableWidth + 10))
    val hyphens = createReportOnSameLine(description).rendered.split("\n").find(_.forall(_ == '-')).map(_.size).get
    hyphens should be(description.split("\n").head.size)
  }

  test(s"The header should contain the string '${XrayReportRenderingTests.description}' if description is nonempty") {
    XrayReportRenderingTests.createReport().rendered should include(XrayReportRenderingTests.description)
  }

  test("If the description contains styles the header should still be centered properly") {
    val descriptionValue = "desc"

    def headerWithDescription(newDescription: String): String =
      XrayReportRenderingTests.createReport(description = newDescription).rendered.split("\n").tail.head

    val headerWithoutStyle = headerWithDescription(descriptionValue)
    val headerWithStyle = headerWithDescription(descriptionValue.yellow)

    def sizeOfLeftPadding(header: String): Int = header.takeWhile(_ != descriptionValue.head).filter(_ == ' ').size

    sizeOfLeftPadding(headerWithoutStyle) should be(sizeOfLeftPadding(headerWithStyle))
  }

  test("The datetime should be rendered in full format") {
    XrayReportRenderingTests.createReport().rendered should include(s"""DateTime | ${new SimpleDateFormat(DateOpsFromSpells.Defaults.Format) format XrayReportRenderingTests.timestamp.getTime}""")
  }

  test("The duration should be readable by humans") {
    XrayReportRenderingTests.createReport().rendered should include(s"Duration | 1 minute 2 seconds")
  }

  test("Location should be equal to the toString implementation of the StackTraceElement") {
    XrayReportRenderingTests.createReport().rendered should include(s"Location | ${XrayReportRenderingTests.stackTraceElement}")
  }

  test("HashCode should be equal to the toString implementation of the hashCode method") {
    XrayReportRenderingTests.createReport().rendered should include(s"HashCode | ${XrayReportRenderingTests.reportValue.hashCode}")
  }

  test("HashCode should not be included in xray report") {
    XrayReportRenderingTests.createReport(reportValue = null).rendered should not include (s"HashCode | ${XrayReportRenderingTests.reportValue.hashCode}")
  }

  test("If type and class are equal the class should not be rendered") {
    XrayReportRenderingTests.createReport().rendered should not include s"Class    | spells.Encoded + Whatever"
  }

  test("If type and class are different they should both be rendered") {
    val typedReport = XrayReportRenderingTests.createReport(reportValue = List(1, 2, 3), typeTag = Some(typeTag[scala.collection.immutable.List[Int]])).rendered

    typedReport should include(s"Class    | scala.collection.immutable.::")
    typedReport should include(s"Type     | List[Int]")
  }

  test("Simple values should be rendered in magenta") {
    XrayReportRenderingTests.createReport().rendered should include(s"Value    | ${"encoded".magenta}")
  }

  test("""If value conains '\n' it should be rendered on the new line""") {
    val newReportString = XrayReportRenderingTests.createReport(reportValue = "firstX\nsecond", typeTag = Some(typeTag[String])).rendered

    newReportString should include(s"Value    | ${"firstX".magenta}")
    newReportString should include(s"         | ${"second".magenta}")
  }

  test("Simple values should be deeply (other styles should not be lost) rendered in magenta") {
    val withStyle = s"enc${"o".green}ded"
    val reportWithStyle = XrayReportRenderingTests.createReport(reportValue = withStyle, typeTag = Some(typeTag[String]))

    reportWithStyle.rendered should include(s"Value    | ${styled(withStyle)(Magenta)}")
  }

  test("Styled values should be deeply (other styles should not be lost) rendered in magenta") {
    val startsWithStyleAndThereforeEndsWithReset = styled("first\nsecond")(Red)
    val newReportString = XrayReportRenderingTests.createReport(reportValue = startsWithStyleAndThereforeEndsWithReset, typeTag = Some(typeTag[String])).rendered

    newReportString should include(s"Value    | ${Magenta.value}${"first".red}")
    newReportString should include(s"         | ${"second".red}")
  }

  test("Nulls should be rendered as values and thus not throw exceptions") {
    XrayReportRenderingTests.createReport(reportValue = null).rendered should include(s"Value    | ${"null".magenta}")
  }

  test(s"Rendered report should contain maximum ${SpellsConfig.terminal.WidthInCharacters} hyphens") {
    val maxWidthInCharacters: Int = SpellsConfig.terminal.WidthInCharacters

    forAll(XrayReportRenderingTests.createReport().rendered split "\n") { line =>
      line.size should be <= maxWidthInCharacters
    }

    val largeReport = XrayReportRenderingTests.createReport(reportValue = ("V" * (maxWidthInCharacters + 20)), typeTag = Some(typeTag[String]))
    val largeLines = largeReport.rendered split "\n"
    val hyphenLines = largeLines.filter(_.forall(_ == '-'))

    forAll(hyphenLines) { hyphenLine =>
      hyphenLine should have size maxWidthInCharacters
    }
  }

  test("Calendards should be rendered as Dates") {
    val date: Date = new Date

    val calendar: Calendar = {
      val c = Calendar.getInstance
      c setTime date
      c
    }

    calendar.rendered should be(date.rendered)
  }

  test("Dates should be rendered as Calendars") {
    val calendar: Calendar = Calendar.getInstance

    val date: Date = calendar.getTime

    date.rendered should be(calendar.rendered)
  }

  test(s"Rendered report should contain exactly 4 lines with hyphens") {
    val maxWidthInCharacters: Int = SpellsConfig.terminal.WidthInCharacters

    val largeReport = XrayReportRenderingTests.createReport(reportValue = ("V" * (maxWidthInCharacters + 20)), typeTag = Some(typeTag[String]))
    val largeLines = largeReport.rendered split "\n"
    val hyphenLines = largeLines.filter(_.forall(_ == '-'))

    hyphenLines.size should be(4)
  }

  test("Custom rendering for java.util.Calendar") {
    val now = Calendar.getInstance

    assume(now != XrayReportRenderingTests.timestamp)

    xrayed(now).rendered should include {
      new SimpleDateFormat(DateOpsFromSpells.Defaults.Format).format(now.getTime).magenta
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

  test("CustomRendering style loss test #infinity") {
    val value = styled("first\nsecond")(Red)
    val inner = xrayed(value)
    val outer = xrayed(inner)

    outer.rendered should include(s"         | ${Magenta.value}Value    | ${Reset.value}${Magenta.value}${Red.value}first")
    outer.rendered should include(s"         | ${Magenta.value}         | ${Reset.value}${Red.value}second")
  }

  test("CustomRendering style loss test #infinity+1") {
    val value = styled("first\nsecond\nthird")(Red)
    val inner = xrayed(value)
    val outer = xrayed(inner)

    outer.rendered should include(s"         | ${Magenta.value}Value    | ${Reset.value}${Magenta.value}${Red.value}first")
    outer.rendered should include(s"         | ${Magenta.value}         | ${Reset.value}${Red.value}second")
    outer.rendered should include(s"         | ${Magenta.value}         | ${Reset.value}${Red.value}third")
  }

  test("Explicit CustomRendering should be picked up") {
    object OwnSource extends CustomRendering {
      override final def rendered(implicit availableWidthInCharacters: spells.StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
        "works"
    }

    xrayed(OwnSource).rendered should include(s"Value    | ${Magenta.value}works")
  }

  test("Additional content for the report") {
    val report = XrayReportRenderingTests.createReport()
    val expected = "Key      | Value"

    report.withAdditionalContent(null).rendered should not include expected
    report.withAdditionalContent(List(null)).rendered should not include expected
    report.withAdditionalContent(List((null, "Value"))).rendered should include("null     | Value")
    report.withAdditionalContent(List(("Key", null))).rendered should include("Key      | null")
    report.withAdditionalContent(List((null, null))).rendered should include("null     | null")
    report.withAdditionalContent(List("Key" -> "Value")).rendered should include(expected)

    val multipleAdditions =
      report.withAdditionalContent(List("Key1" -> "Value1"))
        .withAdditionalContent(List("Key2" -> "Value2"))
        .rendered

    multipleAdditions should include("Key1     | Value1")
    multipleAdditions should include("Key2     | Value2")
  }

  test("Manually passed in additional content") {
    XrayReportRenderingTests.createReport(additionalContent = null)
      .withAdditionalContent(List("Key" -> "Value"))
      .rendered should include("Key      | Value")
  }
}

object XrayReportRenderingTests {
  private def createReport[T: TypeTag](
    reportValue: T = reportValue,
    duration: Duration = duration,
    stackTraceElement: StackTraceElement = stackTraceElement,
    timestamp: Calendar = timestamp,
    description: String = description,
    rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any,
    typeTag: Option[TypeTag[T]] = theTypeTag,
    additionalContent: immutable.Seq[(String, String)] = immutable.Seq.empty
  ): XrayReport[T] =
    new XrayReport[T](
      value = reportValue,
      duration = duration,
      stackTraceElement = stackTraceElement,
      timestamp = timestamp,
      description = description,
      thread = Thread.currentThread,
      rendering = rendering,
      typeTag = typeTag,
      additionalContent = additionalContent
    )

  private lazy val reportValue = new `Encoded + Whatever`
  private lazy val duration = 62.seconds
  private lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
  private lazy val lineNumber = 4711
  private lazy val timestamp = Calendar.getInstance
  private lazy val description = "description"
  private lazy val theTypeTag = Some(typeTag[`Encoded + Whatever`])
}
