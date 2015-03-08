package spells

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

trait Xray {
  this: Ansi with StylePrint with StringOps with AnyOps with HumanRendering =>

  def xrayed[T](expression: => T, description: String = "", style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0)(implicit manifest: reflect.Manifest[T]): XrayResult[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    XrayResult(value, stop.nanos, stackTraceElement, now, description, Thread.currentThread, style)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit class Xray[T](expression: => T)(implicit manifest: reflect.Manifest[T]) {
    def xray(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: XrayResult[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      monitor(result)

      result.value
    }

    def xrayIf(condition: => Boolean)(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: XrayResult[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (condition)
        monitor(result)

      result.value
    }

    def xrayIfResult(conditionFunction: XrayResult[T] => Boolean)(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: XrayResult[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (conditionFunction(result))
        monitor(result)

      result.value
    }
  }

  case class XrayResult[+T](
      value: T,
      duration: Duration,
      stackTraceElement: StackTraceElement,
      timestamp: Calendar,
      description: String,
      thread: Thread,
      style: Ansi#AnsiStyle = Reset)(implicit manifest: reflect.Manifest[T]) {
    override def toString = {
      val lines: Seq[(String, String)] = {
        val content = Vector(
          "DateTime" -> (DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL) format timestamp.getTime),
          "Duration" -> duration.render,
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

        content ++ classOrTypeOrBoth :+ "Value" -> String.valueOf(value) map {
          case (key, value) => key.toString -> value.toString
        }
      }

      val renderedTable = renderTable(lines)

      val numberOfCharsInTheLongestLine =
        renderedTable.map(Ansi.removeStyles).flatMap(_ split "\n").maxBy(_.size).size

      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min spells.terminal.`width-in-characters`)

      val centeredHeader = {
        val header = if (description.isEmpty) "X-Ray" else description
        val emptySpace = hyphens.size - Ansi.removeStyles(header).size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ renderedTable.dropRight(1) ++ Vector(hyphens, renderedTable.last) :+ hyphens

      styled(resultingLines mkString "\n")(style)
    }
  }

  private[spells] def renderTable(in: Seq[(String, String)], styles: Map[String, Ansi#AnsiStyle] = Map("Value" -> Magenta) withDefaultValue Reset): Seq[String] = {
    val sizeOfTheBiggestKey =
      in map {
        case (key, _) => Ansi.removeStyles(key).size
      } max

    val separator = " | "

    val max = spells.terminal.`width-in-characters` - separator.size - sizeOfTheBiggestKey

    in.foldLeft(Vector.empty[String]) {
      case (result, (key, value)) =>
        val keyWithPadding = key.padTo(sizeOfTheBiggestKey, ' ')
        val line = {
          val actualValue = value wrapOnSpaces max

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
                  val theMatch = StylePrint.styleOnly.r findFirstIn takenSoFar.reverse
                  theMatch foreach (result = _)
                  theMatch.isEmpty
                }

                result.toAnsiStyle
              }
              val thisSubLine = styled(subLine)(previousSublineStyle)
              val result = (" " * sizeOfTheBiggestKey) + separator + thisSubLine
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
