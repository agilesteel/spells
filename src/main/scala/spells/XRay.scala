package spells

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

trait Xray {
  import Xray._

  implicit def anyToXRay[T](value: => T)(implicit manifest: reflect.Manifest[T]): Xray.Xray[T] = new Xray.Xray(value)

  def xrayed[T](expression: => T, description: String = "", style: Ansi#AnsiStyle = Reset, increaseStackTraceDepthBy: Int = 0)(implicit manifest: reflect.Manifest[T]): Result[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    Result(value, stop.nanos, stackTraceElement, now.getTime, description, Thread.currentThread, style)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6
}

object Xray {
  class Xray[T](expression: => T)(implicit manifest: reflect.Manifest[T]) {
    def xray(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: Result[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      monitor(result)

      result.value
    }

    def xrayIf(condition: => Boolean)(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: Result[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (condition)
        monitor(result)

      result.value
    }

    def xrayIfResult(conditionFunction: T => Boolean)(implicit description: String = "", style: Ansi#AnsiStyle = Reset, monitor: Result[T] => Unit = Console.println): T = {
      val result = xrayed(expression, description, style, increaseStackTraceDepthBy = +1)

      if (conditionFunction(result.value))
        monitor(result)

      result.value
    }
  }

  case class Result[+T](
      value: T,
      duration: Duration,
      stackTraceElement: StackTraceElement,
      timestamp: Date,
      description: String,
      thread: Thread,
      style: Ansi#AnsiStyle = Reset)(implicit manifest: reflect.Manifest[T]) {
    override def toString = {
      val lines = {
        val content = Vector(
          "DateTime" -> (DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL) format timestamp),
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

        content ++ classOrTypeOrBoth :+ "Value" -> (String.valueOf(value).split("\n").map(styled(_)(Magenta)).mkString("\n"))
      }

      val renderedTable = renderTable(lines)
      val numberOfCharsInTheLongestLine = renderedTable.maxBy { line =>
        if (!(line contains "\n"))
          line.size
        else
          line.split("\n").maxBy(_.size).size
      }.size
      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min spells.terminal.`width-in-characters`)

      val centeredHeader = {
        val header = if (description.isEmpty) "X-Ray" else description
        val emptySpace = hyphens.size - Ansi.removeStyles(header).size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      // val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ renderedTable :+ hyphens
      val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ renderedTable.dropRight(1) ++ Vector(hyphens, renderedTable.last) :+ hyphens

      styled(resultingLines mkString "\n")(style)
    }
  }

  private[spells] def renderTable(in: Seq[(_, _)]): Seq[String] = {
    val stringToString = in map {
      case (key, value) => key.toString -> value.toString
    }

    val sizeOfTheBiggestKey = stringToString.map(_._1.size).max

    stringToString.foldLeft(Vector.empty[String]) {
      case (result, (key, value)) =>
        val keyWithPadding = key.padTo(sizeOfTheBiggestKey, ' ')
        val line = {
          if (!(value contains "\n"))
            keyWithPadding + " | " + value
          else {
            val subLines = value.split("\n").toList
            val renderedHead = keyWithPadding + " | " + subLines.head + "\n"
            val renderedTail = subLines.tail.map((" " * sizeOfTheBiggestKey) + " | " + _).mkString("\n")

            renderedHead + renderedTail
          }
        }

        result :+ line
    }
  }
}
