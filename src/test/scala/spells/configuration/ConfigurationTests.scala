package spells.configuration

import java.util.Calendar
import scala.concurrent.duration._
import scala.reflect.runtime.universe._

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
      lazy val ChosenStyle = AnsiStyle.Random

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
              |        HashCode = \"\"\"${ChosenStyle.value}\"\"\"
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
        def createReport[T: TypeTag](
          value: T = reportValue,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any
        ): XrayReport[T] =
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
      renderedReport should include(reportValue.hashCode in ChosenStyle)
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
              |        HashCode = no
              |        Thread = no
              |        Class = no
              |        Type = no
              |      }
              |    }
              |  }
              |}""".stripMargin
        } withFallback super.loadSpellsConfig

      object StolenFromXrayReportRenderingTests {
        def createReport[T: TypeTag](
          value: T = reportValue,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any
        ): XrayReport[T] =
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

  test("Config validaiton tests") {
    import spells._

    new SpellsConfigModule with StylePrintModule with AnsiModule {
      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          """|spells {
             |  terminal {
             |    WidthInCharacters = -1
             |  }
             |}""".stripMargin
        } withFallback super.loadSpellsConfig

      an[IllegalArgumentException] should be thrownBy {
        val explode = SpellsConfig.terminal.WidthInCharacters.value
      }
    }
  }

  test("Config validaiton error message tests") {
    import spells._

    new SpellsConfigModule with LocationAwarePropertyModule with StylePrintModule with AnsiModule {
      val value = -1

      implicit def materialise(property: LocationAwareProperty[Int]): Int =
        locationAwarePropertyTo(property, value)

      object ValidationProperty extends LocationAwareProperty[Int] {
        override def isValid(in: Int): Boolean = false
      }

      the[IllegalArgumentException] thrownBy {
        val explode = ValidationProperty.value
      } should have message s"requirement failed: ${styled(s"SpellsConfig contains a property: ${ValidationProperty.location.yellow} with an illegal value: ${value.yellow}")(AnsiStyle.Red)}"
    }
  }

  test("Config validaiton custom error message tests") {
    import spells._

    new SpellsConfigModule with StylePrintModule with AnsiModule {
      val value = -1

      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          s"""|spells {
              |  terminal {
              |    WidthInCharacters = -1
              |  }
              |}""".stripMargin
        } withFallback super.loadSpellsConfig

      val property = SpellsConfig.terminal.WidthInCharacters

      the[IllegalArgumentException] thrownBy {
        val explode: Int = property
      } should have message s"requirement failed: ${styled(s"SpellsConfig contains a property: ${property.location.yellow} with an illegal value: ${value.yellow}: ${property.location.yellow} must be ${">= 0".yellow}")(AnsiStyle.Red)}"
    }
  }

  test("Turning off styles") {
    import spells._

    new SpellsConfigModule with StylePrintModule with AnsiModule with ErrorPrintModule {
      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          """|spells {
             |  terminal {
             |    display {
             |      Styles = no
             |    }
             |  }
             |}""".stripMargin
        } withFallback super.loadSpellsConfig

      val rawValue = "test"
      rawValue.green should be(rawValue)
      rawValue in AnsiStyle.Green should be(rawValue)
      styled(rawValue)(AnsiStyle.Green) should be(rawValue)
      erred(rawValue) should be(rawValue)
    }
  }

  test("Short StackTraceElements") {
    new spells.SpellsModule {
      override def loadSpellsConfig: Config =
        ConfigFactory parseString {
          s"""|spells {
              |  custom-rendering {
              |    display {
              |      ShortStackTraceElements = yes
              |    }
              |  }
              |}""".stripMargin
        } withFallback super.loadSpellsConfig

      val nativeMethod = -2
      val basic = new StackTraceElement("declaringClass", "methodName", "fileName", 0)

      basic.rendered should be("(fileName:0)")
      new StackTraceElement("declaringClass", "methodName", "fileName", -1).rendered should be("(fileName)")
      new StackTraceElement("declaringClass", "methodName", "fileName", nativeMethod).rendered should be("(Native Method)")
      new StackTraceElement("declaringClass", "methodName", null, 0).rendered should be("(Unknown Source)")
      new StackTraceElement("declaringClass", "methodName", null, -1).rendered should be("(Unknown Source)")
      new StackTraceElement("declaringClass", "methodName", null, nativeMethod).rendered should be("(Native Method)")

      object StolenFromXrayReportRenderingTests {
        def createReport[T: TypeTag](
          value: T = reportValue,
          duration: Duration = duration,
          stackTraceElement: StackTraceElement = stackTraceElement,
          timestamp: Calendar = timestamp,
          description: String = description,
          rendering: T => spells.CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any
        ): XrayReport[T] =
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

      val report = StolenFromXrayReportRenderingTests.createReport(stackTraceElement = basic)

      report.rendered should include("(fileName:0)")
      report.rendered should not include basic.toString
    }
  }
}
