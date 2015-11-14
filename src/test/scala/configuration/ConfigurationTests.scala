package configuration

import java.util.Calendar
import scala.concurrent.duration._

import com.typesafe.config.{ Config, ConfigFactory }

class ConfigurationTests extends spells.UnitTestConfiguration {
  test("This is how config injection should work") {
    new spells.SpellsModule {
      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          """|spells {
             |  terminal {
             |    WidthInCharacters = 80
             |  }
             |}""".stripMargin
        } withFallback super.loadSpellsConfig

      (SpellsConfig.terminal.WidthInCharacters: Int) should be(80)
    }
  }

  test("XrayReport default rendering should use styles from config") {
    new spells.SpellsModule {
      lazy val ChosenStyle = Ansi.RandomStyle

      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          s"""|spells {
              |  xray {
              |    report {
              |      styles {
              |        Description = \"\"\"${ChosenStyle.value}\"\"\"
              |        DateTime = \"\"\"${ChosenStyle.value}\"\"\"
              |        Duration = \"\"\"${ChosenStyle.value}\"\"\"
              |        Location = \"\"\"${ChosenStyle.value}\"\"\"
              |        Thread = \"\"\"${ChosenStyle.value}\"\"\"
              |        Class = \"\"\"${ChosenStyle.value}\"\"\"
              |        Type = \"\"\"${ChosenStyle.value}\"\"\"
              |        Value = \"\"\"${ChosenStyle.value}\"\"\"
              |      }
              |    }
              |  }
              |}""".stripMargin
        } withFallback super.loadSpellsConfig

      object StolenFromXrayReportRenderingTests {
        def createReport[T: Manifest](
          value: T = reportValue,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayReport[T] =
          new XrayReport[T](
            value = value,
            duration = duration,
            stackTraceElement = stackTraceElement,
            timestamp = timestamp,
            description = description,
            thread = thread,
            rendering = rendering
          )

        lazy val reportValue = 10
        lazy val duration = 62.seconds
        lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
        lazy val lineNumber = 4711
        lazy val timestamp = Calendar.getInstance
        lazy val description = "description"
        lazy val thread = Thread.currentThread
      }

      import StolenFromXrayReportRenderingTests._
      val renderedReport = createReport().rendered

      renderedReport should include(reportValue in ChosenStyle)
      renderedReport should include(duration.rendered in ChosenStyle)
      renderedReport should include(stackTraceElement in ChosenStyle)
      renderedReport should include(timestamp.rendered in ChosenStyle)
      renderedReport should include(description in ChosenStyle)
      renderedReport should include(thread in ChosenStyle)
    }
  }

  test("XrayReport default rendering should be tweak-able from the config") {
    new spells.SpellsModule {
      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          s"""|spells {
              |  xray {
              |    report {
              |      display {
              |        DateTime = no
              |        Duration = no
              |        Location = no
              |        Thread = no
              |        Class = no
              |        Type = no
              |      }
              |    }
              |  }
              |}""".stripMargin
        } withFallback super.loadSpellsConfig

      object StolenFromXrayReportRenderingTests {
        def createReport[T: Manifest](
          value: T = reportValue,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayReport[T] =
          new XrayReport[T](
            value = value,
            duration = duration,
            stackTraceElement = stackTraceElement,
            timestamp = timestamp,
            description = description,
            thread = thread,
            rendering = rendering
          )

        lazy val reportValue = 10
        lazy val duration = 62.seconds
        lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
        lazy val lineNumber = 4711
        lazy val timestamp = Calendar.getInstance
        lazy val description = "description"
        lazy val thread = Thread.currentThread
        lazy val `class` = "java.lang.Integer"
        lazy val `type` = "Int"
      }

      import StolenFromXrayReportRenderingTests._
      val renderedReport = createReport().rendered

      renderedReport should not include timestamp.rendered
      renderedReport should not include duration.rendered
      renderedReport should not include stackTraceElement.toString
      renderedReport should not include thread.toString
      renderedReport should not include `class`
      renderedReport should not include s"| ${`type`}"

      val maxWidthInCharacters: Int = SpellsConfig.terminal.WidthInCharacters

      val largeReport = createReport(value = ("V" * (maxWidthInCharacters + 20)))
      val largeLines = largeReport.rendered split "\n"
      val hyphenLines = largeLines.filter(_.forall(_ == '-'))

      hyphenLines.size should be(3)
    }
  }
}
