package spells

trait HumanRendering {
  import HumanRendering._

  implicit def byteToRendering(value: Byte): Rendering = Rendering(value)
  implicit def shortToRendering(value: Short): Rendering = Rendering(value)
  implicit def intToRendering(value: Int): Rendering = Rendering(value)
  implicit def longToRendering(value: Long): Rendering = Rendering(value)
  implicit def durationToRendering(value: scala.concurrent.duration.Duration): DurationRendering = DurationRendering(value)

  case class DurationRendering(value: scala.concurrent.duration.Duration) {
    def render: String = value.toNanos.render.duration.from.nanoseconds
  }

  case class Rendering(value: Long) {
    def render: Rendering = this

    object duration {
      lazy val nanoseconds: String = render(Nanosecond)
      lazy val milliseconds: String = render(Millisecond)
      lazy val seconds: String = render(Second)
      lazy val minutes: String = render(Minute)
      lazy val hours: String = render(Hour)
      lazy val days: String = render(Day)
      lazy val months: String = render(Month)
      lazy val years: String = render(Year)

      private def render(unit: String) =
        if (valueDoesNotEndWithElevenButEndsWithOne) s"$value $unit" else s"$value ${unit}s"

      private lazy val valueDoesNotEndWithElevenButEndsWithOne: Boolean = {
        val stringValue = value.toString
        val doesNotEndWithEleven = !(stringValue endsWith "11")
        val endsWithOne = stringValue endsWith "1"

        doesNotEndWithEleven && endsWithOne
      }

      object from {
        lazy val months: String = render(duration.months, Duration(months = value))
        lazy val hours: String = render(duration.hours, Duration(hours = value))
        lazy val minutes: String = render(duration.minutes, Duration(minutes = value))
        lazy val seconds: String = render(duration.seconds, Duration(seconds = value))
        lazy val milliseconds: String = render(duration.milliseconds, Duration(milliseconds = value))
        lazy val nanoseconds: String = render(duration.nanoseconds, Duration(nanoseconds = value))

        private def render(alreadyRendered: String, toRender: => Duration): String =
          if (value == 0) alreadyRendered else toRender.toString
      }
    }
  }
}

object HumanRendering {
  val Nanosecond = "nanosecond"
  val Millisecond = "millisecond"
  val Second = "second"
  val Minute = "minute"
  val Hour = "hour"
  val Day = "day"
  val Month = "month"
  val Year = "year"

  private case class Duration(
      years: Long = 0,
      months: Long = 0,
      days: Long = 0,
      hours: Long = 0,
      minutes: Long = 0,
      seconds: Long = 0,
      milliseconds: Long = 0,
      nanoseconds: Long = 0) {
    override val toString =
      if (calculated contains "-") s"""-${calculated.replaceAll("-", "")}""" else calculated

    private lazy val calculated = {
      if (years != 0)
        years.render.duration.years
      else if (months != 0) {
        val (quotient, remainder) = division(months, 12)

        render(
          wholeNumber = copy(years = quotient, months = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.months
        )
      } else if (days != 0)
        days.render.duration.days
      else if (hours != 0) {
        val (quotient, remainder) = division(hours, 24)

        render(
          wholeNumber = copy(days = quotient, hours = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.hours
        )
      } else if (minutes != 0) {
        val (quotient, remainder) = division(minutes, 60)

        render(
          wholeNumber = copy(hours = quotient, minutes = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.minutes
        )
      } else if (seconds != 0) {
        val (quotient, remainder) = division(seconds, 60)

        render(
          wholeNumber = copy(minutes = quotient, seconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.seconds
        )
      } else if (milliseconds != 0) {
        val (quotient, remainder) = division(milliseconds, 1000)

        render(
          wholeNumber = copy(seconds = quotient, milliseconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.milliseconds
        )
      } else if (nanoseconds != 0) {
        val (quotient, remainder) = division(nanoseconds, 1000000)

        render(
          wholeNumber = copy(milliseconds = quotient, nanoseconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.nanoseconds
        )
      } else ""
    }

    private def division(dividend: Long, divisor: Long): (Long, Long) = {
      val quotient = dividend / divisor
      val remainder = dividend % divisor

      quotient -> remainder
    }

    private def render(wholeNumber: Duration, remainder: Long, renderedRemainder: String): String =
      if (wholeNumber == Duration()) renderedRemainder else s"$wholeNumber${potentialRest(remainder, renderedRemainder)}"

    private def potentialRest(remainder: Long, renderedRemainder: String): String =
      if (remainder == 0) "" else s" $renderedRemainder"
  }
}
