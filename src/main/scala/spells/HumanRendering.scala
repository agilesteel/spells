package spells

trait HumanRendering {
  implicit def byteToRendering(value: Byte): Rendering = Rendering(value)
  implicit def shortToRendering(value: Short): Rendering = Rendering(value)
  implicit def intToRendering(value: Int): Rendering = Rendering(value)
  implicit def longToRendering(value: Long): Rendering = Rendering(value)

  case class Rendering(value: Long) {
    def render: Rendering = this

    object duration {
      lazy val months: String = render(duration.just.months, Duration(months = value))
      lazy val days: String = render(duration.just.days, Duration(days = value))
      lazy val hours: String = render(duration.just.hours, Duration(hours = value))
      lazy val minutes: String = render(duration.just.minutes, Duration(minutes = value))
      lazy val seconds: String = render(duration.just.seconds, Duration(seconds = value))
      lazy val milliseconds: String = render(duration.just.milliseconds, Duration(milliseconds = value))
      lazy val nanoseconds: String = render(duration.just.nanoseconds, Duration(nanoseconds = value))

      private def render(alreadyRendered: String, toRender: => Duration): String =
        if (value == 0) alreadyRendered else toRender.toString

      object just {
        lazy val nanoseconds: String = render(Nanosecond)
        lazy val milliseconds: String = render(Millisecond)
        lazy val seconds: String = render(Second)
        lazy val minutes: String = render(Minute)
        lazy val hours: String = render(Hour)
        lazy val days: String = render(Day)
        lazy val weeks: String = render(Week)
        lazy val months: String = render(Month)
        lazy val years: String = render(Year)

        private def render(unit: String) =
          if (value == 1) s"$value $unit" else s"$value ${unit}s"
      }
    }
  }

  val Nanosecond = "nanosecond"
  val Millisecond = "millisecond"
  val Second = "second"
  val Minute = "minute"
  val Hour = "hour"
  val Day = "day"
  val Week = "week"
  val Month = "month"
  val Year = "year"

  private case class Duration(
      years: Long = 0,
      months: Long = 0,
      weeks: Long = 0,
      days: Long = 0,
      hours: Long = 0,
      minutes: Long = 0,
      seconds: Long = 0,
      milliseconds: Long = 0,
      nanoseconds: Long = 0) {
    override val toString = if (isPositiveOrZero) calculated else recalculated

    private lazy val isPositiveOrZero: Boolean =
      Vector(years, months, weeks, days, hours, minutes, seconds, milliseconds, nanoseconds) forall (_ >= 0)

    private lazy val calculated = {
      if (years != 0)
        years.render.duration.just.years
      else if (months != 0) {
        val (quotient, remainder) = division(months, 12)

        render(
          wholeDuration = copy(years = quotient, months = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.months
        )
      } else if (weeks != 0)
        weeks.render.duration.just.weeks

      else if (days != 0) {
        val (quotient, remainder) = division(days, 7)

        render(
          wholeDuration = copy(weeks = quotient, days = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.days
        )
      } else if (hours != 0) {
        val (quotient, remainder) = division(hours, 24)

        render(
          wholeDuration = copy(days = quotient, hours = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.hours
        )
      } else if (minutes != 0) {
        val (quotient, remainder) = division(minutes, 60)

        render(
          wholeDuration = copy(hours = quotient, minutes = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.minutes
        )
      } else if (seconds != 0) {
        val (quotient, remainder) = division(seconds, 60)

        render(
          wholeDuration = copy(minutes = quotient, seconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.seconds
        )
      } else if (milliseconds != 0) {
        val (quotient, remainder) = division(milliseconds, 1000)

        render(
          wholeDuration = copy(seconds = quotient, milliseconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.milliseconds
        )
      } else if (nanoseconds != 0) {
        val (quotient, remainder) = division(nanoseconds, 1000000)

        render(
          wholeDuration = copy(milliseconds = quotient, nanoseconds = 0),
          remainder = remainder,
          renderedRemainder = remainder.render.duration.just.nanoseconds
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
      else if (currentMetric startsWith Week)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour, appliedTo =
                  replace1MetricsWith1Metric(Day))))))
      else if (currentMetric startsWith Month)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour, appliedTo =
                  replace1MetricsWith1Metric(Day, appliedTo =
                    replace1MetricsWith1Metric(Week)))))))
      else if (currentMetric startsWith Year)
        replace1MetricsWith1Metric(Nanosecond, appliedTo =
          replace1MetricsWith1Metric(Millisecond, appliedTo =
            replace1MetricsWith1Metric(Second, appliedTo =
              replace1MetricsWith1Metric(Minute, appliedTo =
                replace1MetricsWith1Metric(Hour, appliedTo =
                  replace1MetricsWith1Metric(Day, appliedTo =
                    replace1MetricsWith1Metric(Week, appliedTo =
                      replace1MetricsWith1Metric(Month))))))))
      else originalCalculationWithASingleMinusSign
    }
  }
}
