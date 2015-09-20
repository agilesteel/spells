package spells

import java.util.Calendar
import scala.concurrent.duration._

trait Xray {
  this: Ansi =>

  final def xrayed[T](expression: => T, description: Xray.Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit manifest: Manifest[T], style: Ansi.Style = Reset, rendering: T => CustomRendering = CustomRendering.Defaults.Any): Xray.Report[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    new Xray.Report(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style, rendering)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit class XrayFromSpells[T](expression: => T)(implicit manifest: Manifest[T], style: Ansi.Style = Reset, rendering: T => CustomRendering = CustomRendering.Defaults.Any, monitor: Xray.Report[T] => Unit = (report: Xray.Report[T]) => Console.println(report.rendered)) {
    def xray(implicit description: Xray.Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(manifest, style, rendering)

      monitor(report)

      report.value
    }

    def xrayIf(conditionFunction: Xray.Report[T] => Boolean)(implicit description: Xray.Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(manifest, style, rendering)

      if (conditionFunction(report))
        monitor(report)

      report.value
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
      rendering: T => CustomRendering = CustomRendering.Defaults.Any) extends spells.CustomRendering {
    override final def rendered: String = {
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

        content ++ classOrTypeOrBoth :+ "Value" -> (Option(value).fold("null")(rendering(_).rendered)) map {
          case (key, value) => key.toString -> value.toString
        }
      }

      val table = renderedTable4Xray(lines, styles = Map("Value" -> Magenta) withDefaultValue Reset)

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

    private[spells] final def renderedTable4Xray(in: Seq[(String, String)], styles: Map[String, Ansi.Style] = Map.empty withDefaultValue Reset): Seq[String] = {
      if (in.isEmpty) {
        println("I'm here")
        Seq.empty
      } else {
        val sizeOfTheBiggestKey = in map {
          case (key, _) => Ansi.removeStyles(key).size
        } max

        val separator = " | "

        val maxWidthInCharacters =
          spells.terminal.`width-in-characters` - separator.size - sizeOfTheBiggestKey

        in.foldLeft(Vector.empty[String]) {
          case (result, (key, value)) =>
            val keyWithPadding = key.padTo(sizeOfTheBiggestKey, ' ')
            val line = {
              val actualValue = value wrappedOnSpaces maxWidthInCharacters

              if (!(actualValue contains "\n"))
                keyWithPadding + separator + styled(actualValue)(styles(key))
              else {
                val subLines = actualValue.split("\n").toList
                val renderedHead = keyWithPadding + separator + styled(subLines.head)(styles(key)) + "\n"

                var previousSubLine = styled(subLines.head)(styles(key))

                val renderedTail = subLines.tail.map { subLine =>
                  val previousSublineStyle = {
                    var takenSoFar = ""
                    var result = ""

                    previousSubLine.reverse.takeWhile { char =>
                      takenSoFar += char
                      val theMatch = StylePrint.styleOrReset.r findFirstIn takenSoFar.reverse
                      theMatch foreach (result = _)
                      theMatch.isEmpty
                    }

                    result.toAnsiStyle
                  }

                  val thisSubLine =
                    if (previousSublineStyle.value == Reset.value)
                      styled(subLine)(styles(key))
                    else
                      styled(subLine)(previousSublineStyle)

                  val result = (" " * sizeOfTheBiggestKey) + separator + thisSubLine

                  if (thisSubLine.nonEmpty)
                    previousSubLine = thisSubLine

                  result
                }.mkString("\n")

                renderedHead + renderedTail
              }
            }

            result :+ line
        }
      }
    }
  }
}

