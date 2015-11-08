package configuration

import java.util.Calendar
import scala.concurrent.duration._

import com.typesafe.config.{ Config, ConfigFactory }

class ConfigurationTests extends spells.UnitTestConfiguration {
  test("This is how config injection should work") {
    new spells.Spells {
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

  test("Xray should use styles from config") {
    new spells.Spells {
      lazy val ChosenStyle = spells.Ansi.Random

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
          value: T = value,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          style: spells.Ansi.Style = Reset,
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

        private lazy val value = 10
        private lazy val duration = 62.seconds
        private lazy val stackTraceElement = new StackTraceElement("declaringClass", "methodName", "fileName", lineNumber)
        private lazy val lineNumber = 4711
        private lazy val timestamp = Calendar.getInstance
        private lazy val description = "description"
      }

      StolenFromXrayReportRenderingTests.createReport(description = "whatup").rendered should include("whatup" in ChosenStyle)
    }
  }
}
