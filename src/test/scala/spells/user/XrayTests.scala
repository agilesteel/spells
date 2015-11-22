package spells.user

import java.text.SimpleDateFormat
import java.util.Calendar

import AnsiStyle._

class XrayTests extends spells.UnitTestConfiguration {
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
      val condition: spells.XrayModule#XrayReport[Int] => Boolean = _ => true

      input.xrayIf(condition)
      wasMonitorCalled should be(true)
    }
  }

  test("Monitor should not be called for xrayIf if the condition yields false") {
    new MonitoringEnvironement {
      val condition: spells.XrayModule#XrayReport[Int] => Boolean = _ => false

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
      implicit def monitor(report: XrayReport[Any]): Unit =
        report.rendered should startWith(color.value)

      10.xray
    }
  }

  test("It should not be possible to implicitly pass in descriptions of type String to xrayed") {
    implicit val description: String = "description"

    xrayed("value").description should be(Xray.Defaults.Description.toString)
  }

  test("It should not be possible to implicitly pass in descriptions of type String to xrat") {
    implicit val description: String = "description"

    SilentOutputStream out {
      implicit def monitor(report: XrayReport[Any]): Unit =
        report.description should be(Xray.Defaults.Description.toString)

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
      implicit def monitor(report: XrayReport[Any]): Unit =
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
      implicit def monitor(report: XrayReport[Any]): Unit = {
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
  implicit def monitor[T](report: spells.XrayModule#XrayReport[T]): Unit = wasMonitorCalled = true
}

