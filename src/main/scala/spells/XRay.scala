package spells

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

trait Xray {
  implicit def anyToXRay[T](value: => T): Xray.Xray[T] = new Xray.Xray(value)
}

object Xray {
  class Xray[T](expression: => T) {
    def xray(): T = {
      val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy = -2)

      val now = Calendar.getInstance

      val start = System.nanoTime
      val value = expression
      val stop = System.nanoTime - start

      println(Result(value, stop.nanos, stackTraceElement, now.getTime))

      value
    }
  }

  case class Result[T](value: T, duration: Duration, stackTraceElement: StackTraceElement, timestamp: Date) {
    override def toString = {
      val content =
        s"""~DateTime | ${DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL) format timestamp}
            ~Duration | ${duration.render}
            ~Location | $stackTraceElement
            ~Type     | ${value.decodedClassName}
            ~Value    | %s""".stripMargin('~')

      val header = "X-Ray"
      val numberOfCharsInTheLongestLine = render(content).split("\n").maxBy(_.size).size
      val hyphens = "-" * ((numberOfCharsInTheLongestLine - 1).min(80))
      val centeredHeader = {
        val emptySpace = hyphens.size - header.size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      val result =
        s"""~$hyphens
            ~$centeredHeader
            ~$hyphens
            ~$content
            ~$hyphens"""

      render(result)
    }

    private def render(string: String): String =
      string.stripMargin('~').format(String.valueOf(value).magenta)
  }
}
