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
        if (value == 1) s"$value $unit" else s"$value ${unit}s"

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
    override val toString = if (isPositiveOrZero) calculated else recalculated

    private lazy val isPositiveOrZero: Boolean =
      Vector(years, months, days, hours, minutes, seconds, milliseconds, nanoseconds) forall (_ >= 0)

    private lazy val calculated = {
      if (years != 0)
        years.render.duration.years
      else if (months != 0) {
        val (quotient, remainder) = division(months, 12)

        render(
          wholeDuration = copy(years = quotient, months = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.months
        )
      } else if (days != 0)
        days.render.duration.days
      else if (hours != 0) {
        val (quotient, remainder) = division(hours, 24)

        render(
          wholeDuration = copy(days = quotient, hours = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.hours
        )
      } else if (minutes != 0) {
        val (quotient, remainder) = division(minutes, 60)

        render(
          wholeDuration = copy(hours = quotient, minutes = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.minutes
        )
      } else if (seconds != 0) {
        val (quotient, remainder) = division(seconds, 60)

        render(
          wholeDuration = copy(minutes = quotient, seconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.seconds
        )
      } else if (milliseconds != 0) {
        val (quotient, remainder) = division(milliseconds, 1000)

        render(
          wholeDuration = copy(seconds = quotient, milliseconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.milliseconds
        )
      } else if (nanoseconds != 0) {
        val (quotient, remainder) = division(nanoseconds, 1000000)

        render(
          wholeDuration = copy(milliseconds = quotient, nanoseconds = 0),
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

    private def render(wholeDuration: Duration, remainder: Long, renderedRemainder: String): String =
      if (wholeDuration == Duration()) renderedRemainder else s"$wholeDuration${potentialRest(remainder, renderedRemainder)}"

    private def potentialRest(remainder: Long, renderedRemainder: String): String =
      if (remainder == 0) "" else s" $renderedRemainder"

    private lazy val recalculated = {
      val originalCalculationWithASingleMinusSign = s"""-${calculated.replaceAll("-", "")}"""

      val currentMetric = originalCalculationWithASingleMinusSign.split(" ")(1)

      def replace1MetricsWith1Metric(metric: String, appliedTo: String = originalCalculationWithASingleMinusSign): String =
        appliedTo.replaceAll(s"1 ${metric}s", s"1 $metric")

      if (currentMetric startsWith Millisecond)
        replace1MetricsWith1Metric(Nanosecond)
      else if (currentMetric startsWith Second)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond))
      else if (currentMetric startsWith Minute)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second)))
      else if (currentMetric startsWith Hour)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute))))
      else if (currentMetric startsWith Day)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour)))))
      else if (currentMetric startsWith Month)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour, appliedTo =
                  replace1MetricsWith1Metric(Day))))))
      else if (currentMetric startsWith Year)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour, appliedTo =
                  replace1MetricsWith1Metric(Day, appliedTo =
                    replace1MetricsWith1Metric(Month)))))))
      else originalCalculationWithASingleMinusSign
    }
  }
}
