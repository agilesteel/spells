package spells

/** Provides human readable renderings for numeric types. */
trait HumanRenderingModule {
  final implicit def byteToRendering(value: Byte): Rendering = Rendering(value)
  final implicit def shortToRendering(value: Short): Rendering =
    Rendering(value)
  final implicit def intToRendering(value: Int): Rendering = Rendering(value)
  final implicit def longToRendering(value: Long): Rendering = Rendering(value)

  case class Rendering(value: Long) {
    final def render: Rendering = this

    object duration {

      /** Renders months as follows `13.render.duration.months // 1 year 1 month` */
      final lazy val months: String =
        render(duration.just.months, Duration(months = value))

      /** Renders days as follows `7.render.duration.days // 1 week 1 day` */
      final lazy val days: String =
        render(duration.just.days, Duration(days = value))

      /** Renders hours as in `25.render.duration.hours // 1 day 1 hour` */
      final lazy val hours: String =
        render(duration.just.hours, Duration(hours = value))

      /** Renders minutes as in `61.render.duration.minutes // 1 hour 1 minute` */
      final lazy val minutes: String =
        render(duration.just.minutes, Duration(minutes = value))

      /** Renders seconds as in `61.render.duration.seconds // 1 minute 1 second` */
      final lazy val seconds: String =
        render(duration.just.seconds, Duration(seconds = value))

      /** Renders milliseconds as in `1001.render.duration.milliseconds // 1 second 1 millisecond` */
      final lazy val milliseconds: String =
        render(duration.just.milliseconds, Duration(milliseconds = value))

      /** Renders nanoseconds as in `1000001.render.duration.nanoseconds // 1 millisecond 1 nanosecond` */
      final lazy val nanoseconds: String =
        render(duration.just.nanoseconds, Duration(nanoseconds = value))

      final private def render(
          alreadyRendered: String,
          toRender: => Duration
        ): String =
        if (value == 0) alreadyRendered else toRender.toString

      /** Renders just what is requested
        * {{{
        * 1.render.duration.just.seconds // 1 second
        * 61.render.duration.just.seconds // 61 seconds (instead of 1 minute 1 second)
        * }}}
        */
      object just {
        final lazy val years: String = render(Year)
        final lazy val months: String = render(Month)
        final lazy val weeks: String = render(Week)
        final lazy val days: String = render(Day)
        final lazy val hours: String = render(Hour)
        final lazy val minutes: String = render(Minute)
        final lazy val seconds: String = render(Second)
        final lazy val milliseconds: String = render(Millisecond)
        final lazy val nanoseconds: String = render(Nanosecond)

        final private def render(unit: String): String =
          if (value == 1) s"$value $unit" else s"$value ${unit}s"
      }
    }
  }

  final private val Nanosecond: String = "nanosecond"
  final private val Millisecond: String = "millisecond"
  final private val Second: String = "second"
  final private val Minute: String = "minute"
  final private val Hour: String = "hour"
  final private val Day: String = "day"
  final private val Week: String = "week"
  final private val Month: String = "month"
  final private val Year: String = "year"

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

    final private lazy val isPositiveOrZero: Boolean =
      Vector(
        years,
        months,
        weeks,
        days,
        hours,
        minutes,
        seconds,
        milliseconds,
        nanoseconds
      ) forall (_ >= 0)

    final private lazy val calculated = {
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

    final private def division(dividend: Long, divisor: Long): (Long, Long) = {
      val quotient = dividend / divisor
      val remainder = dividend % divisor

      quotient -> remainder
    }

    final private def render(
        wholeDuration: Duration,
        remainder: Long,
        renderedRemainder: String
      ): String =
      if (wholeDuration == Duration()) renderedRemainder
      else s"$wholeDuration${potentialRest(remainder, renderedRemainder)}"

    final private def potentialRest(
        remainder: Long,
        renderedRemainder: String
      ): String =
      if (remainder == 0) "" else s" $renderedRemainder"

    final private lazy val recalculated = {
      val originalCalculationWithASingleMinusSign =
        s"""-${calculated.replaceAll("-", "")}"""

      val currentMetric = originalCalculationWithASingleMinusSign.split(" ")(1)

      def replace1MetricsWith1Metric(
          metric: String,
          appliedTo: String = originalCalculationWithASingleMinusSign
        ): String =
        appliedTo.replaceAll(s"1 ${metric}s", s"1 $metric")

      if (currentMetric startsWith Millisecond)
        replace1MetricsWith1Metric(Nanosecond)
      else if (currentMetric startsWith Second)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(Millisecond)
        )
      else if (currentMetric startsWith Minute)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(Second)
          )
        )
      else if (currentMetric startsWith Hour)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(
              Second,
              appliedTo = replace1MetricsWith1Metric(Minute)
            )
          )
        )
      else if (currentMetric startsWith Day)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(
              Second,
              appliedTo = replace1MetricsWith1Metric(
                Minute,
                appliedTo = replace1MetricsWith1Metric(Hour)
              )
            )
          )
        )
      else if (currentMetric startsWith Week)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(
              Second,
              appliedTo = replace1MetricsWith1Metric(
                Minute,
                appliedTo = replace1MetricsWith1Metric(
                  Hour,
                  appliedTo = replace1MetricsWith1Metric(Day)
                )
              )
            )
          )
        )
      else if (currentMetric startsWith Month)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(
              Second,
              appliedTo = replace1MetricsWith1Metric(
                Minute,
                appliedTo = replace1MetricsWith1Metric(
                  Hour,
                  appliedTo = replace1MetricsWith1Metric(
                    Day,
                    appliedTo = replace1MetricsWith1Metric(Week)
                  )
                )
              )
            )
          )
        )
      else if (currentMetric startsWith Year)
        replace1MetricsWith1Metric(
          Nanosecond,
          appliedTo = replace1MetricsWith1Metric(
            Millisecond,
            appliedTo = replace1MetricsWith1Metric(
              Second,
              appliedTo = replace1MetricsWith1Metric(
                Minute,
                appliedTo = replace1MetricsWith1Metric(
                  Hour,
                  appliedTo = replace1MetricsWith1Metric(
                    Day,
                    appliedTo = replace1MetricsWith1Metric(
                      Week,
                      appliedTo = replace1MetricsWith1Metric(Month)
                    )
                  )
                )
              )
            )
          )
        )
      else originalCalculationWithASingleMinusSign
    }
  }
}
