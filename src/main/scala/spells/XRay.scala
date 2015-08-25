package spells

import java.util.Calendar
import scala.concurrent.duration._

trait Xray {
  this: Ansi =>

  final def xrayed[T](expression: => T, description: Xray.Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit manifest: Manifest[T], style: Ansi.Style = Reset, evidence: T => CustomRendering = CustomRendering.Defaults.Any): Xray.Report[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    new Xray.Report(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style, evidence)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit class XrayFromSpells[T](expression: => T)(implicit manifest: Manifest[T], style: Ansi.Style = Reset, evidence: T => CustomRendering = CustomRendering.Defaults.Any, monitor: Xray.Report[T] => Unit = (x: Xray.Report[T]) => Console.println(x)) {
    def xray(implicit description: Xray.Description = Xray.Defaults.Description): T = {
      val result = xrayed(expression, description, increaseStackTraceDepthBy = +1)(manifest, style, evidence)

      monitor(result)

      result.value
    }

    def xrayIf(conditionFunction: Xray.Report[T] => Boolean)(implicit description: Xray.Description = Xray.Defaults.Description): T = {
      val result = xrayed(expression, description, increaseStackTraceDepthBy = +1)(manifest, style, evidence)

      if (conditionFunction(result))
        monitor(result)

      result.value
    }
  }

}

object Xray
    extends Xray
    with Ansi
    with AnyOps
    with CalendarOps
    with DateOps
    with DurationOps
    with HumanRendering
    with StringOps
    with StylePrint
    with TraversableOps {
  object Defaults {
    final val Description: Xray.Description = new Xray.Description("X-Ray")
  }

  implicit class Description(val value: String) extends AnyVal {
    override final def toString: String = value
  }

  final class Report[+T: Manifest](
      final val value: T,
      final val duration: Duration,
      final val stackTraceElement: StackTraceElement,
      final val timestamp: Calendar,
      final val description: String,
      final val thread: Thread,
      final val style: Ansi.Style = Reset,
      evidence: T => CustomRendering = CustomRendering.Defaults.Any) {
    override final def toString = {
      val lines: Seq[(String, String)] = {
        val content = Vector(
          "DateTime" -> timestamp.rendered,
          "Duration" -> duration.rendered,
          "Location" -> stackTraceElement,
          "Thread" -> thread
        )

        val classOrTypeOrBoth = {
          val `class` = value.decodedClassName
          val `type` = manifest.toString.withDecodedScalaSymbols

          if (`class` == `type`)
            Vector("Type" -> `type`)
          else
            Vector("Class" -> `class`, "Type" -> `type`)
        }

        content ++ classOrTypeOrBoth :+ "Value" -> (Option(value).fold("null")(evidence(_).rendered)) map {
          case (key, value) => key.toString -> value.toString
        }
      }

      val table = renderedTable(lines, styles = Map("Value" -> Magenta) withDefaultValue Reset)

      val numberOfCharsInTheLongestLine =
        table.map(Ansi.removeStyles).flatMap(_ split "\n").maxBy(_.size).size

      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min spells.terminal.`width-in-characters`)

      val centeredHeader = {
        val header = if (Ansi.removeStyles(description).isEmpty) "X-Ray".green else styled(description)(Green)
        val emptySpace = hyphens.size - Ansi.removeStyles(header).size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ table.dropRight(1) ++ Vector(hyphens, table.last) :+ hyphens

      styled(resultingLines mkString "\n")(style)
    }
  }
}
