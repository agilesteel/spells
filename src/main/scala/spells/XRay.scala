package spells

import java.text.DateFormat
import java.util.{ Calendar, Date }
import scala.concurrent.duration._

trait XRay {
  implicit def anyToXRay[T](value: => T): XRay.XRay[T] = new XRay.XRay(value)
}

object XRay {
  class XRay[T](expression: => T) {
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
      val result =
        s"""~Date     | ${DateFormat.getDateInstance(DateFormat.FULL) format timestamp}
            ~Time     | ${DateFormat.getTimeInstance(DateFormat.FULL) format timestamp}
            ~Duration | ${duration.render}
            ~Location | $stackTraceElement
            ~Type     | ${value.decodedName}
            ~Value    | ${value.magenta}""".stripMargin('~')

      val header = "X-Ray"
      val numberOfCharsInTheLongestLine = result.split("\n").maxBy(_.size).size
      val hyphens = "-" * ((numberOfCharsInTheLongestLine - 1).min(80))
      val centeredHeader = {
        val emptySpace = hyphens.size - header.size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      s"""~$hyphens
          ~$centeredHeader
          ~$hyphens
          ~$result
          ~$hyphens""".stripMargin('~')
    }
  }
}

object Main extends App {
  (List.fill(80)(0)).xray
}
