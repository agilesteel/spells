package spells

import java.util.Calendar
import scala.concurrent.duration._

import Xray._

trait Xray {
  this: Ansi with AnyOps with CalendarOps with DurationOps with HumanRendering with StringOps with StylePrint with TraversableOps =>

  final def xrayed[T](expression: => T, description: Xray.Description = Defaults.Description, style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0)(implicit manifest: Manifest[T], evidence: T => CustomRendering = CustomRendering.Default.apply(_: T)): XrayReport[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    XrayReport(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit class XrayFromSpells[T](expression: => T)(implicit manifest: Manifest[T]) {
    final def xray(implicit description: Description = Defaults.Description, style: Ansi#AnsiStyle = Reset, monitor: XrayReport[T] => Unit = Console.println, evidence: T => CustomRendering = CustomRendering.Default.apply): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      monitor(result)

      result.value
    }

    final def xrayIf(condition: => Boolean)(implicit description: Description = Defaults.Description, style: Ansi#AnsiStyle = Reset, monitor: XrayReport[T] => Unit = Console.println, evidence: T => CustomRendering = CustomRendering.Default.apply): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (condition)
        monitor(result)

      result.value
    }

    final def xrayIfResult(conditionFunction: XrayReport[T] => Boolean)(implicit description: Description = Defaults.Description, style: Ansi#AnsiStyle = Reset, monitor: XrayReport[T] => Unit = Console.println, evidence: T => CustomRendering = CustomRendering.Default.apply): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (conditionFunction(result))
        monitor(result)

      result.value
    }
  }

  case class XrayReport[+T](
      value: T,
      duration: Duration,
      stackTraceElement: StackTraceElement,
      timestamp: Calendar,
      description: String,
      thread: Thread,
      style: Ansi#AnsiStyle = Reset)(implicit manifest: Manifest[T], evidence: T => CustomRendering = CustomRendering.Default.apply(_: T)) {
    override def toString = {
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

        content ++ classOrTypeOrBoth :+ "Value" -> (Option(value).fold("null")(_.rendered)) map {
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

object Xray {
  object Defaults {
    val Description: Xray.Description = new Xray.Description("X-Ray")
  }

  implicit class Description(val value: String) extends AnyVal {
    override def toString: String = value
  }
}
