package spells.user

class HumanRenderingTests extends UnitTestConfiguration {
  test("""It should be possible to call render on a byte, short, int, long, double, scala.concurrent.duration""") {
    (1: Byte).render should be(Rendering(1))
    (1: Short).render should be(Rendering(1))
    1.render should be(Rendering(1))
    1L.render should be(Rendering(1))

    import scala.concurrent.duration._
    1.day.rendered should be(1.render.duration.days)
  }

  test("""It should be possible to render a single duration""") {
    1.render.duration.just.nanoseconds should be("1 nanosecond")
    -1.render.duration.just.nanoseconds should be("-1 nanoseconds")

    1.render.duration.just.milliseconds should be("1 millisecond")
    -1.render.duration.just.milliseconds should be("-1 milliseconds")

    1.render.duration.just.seconds should be("1 second")
    -1.render.duration.just.seconds should be("-1 seconds")

    1.render.duration.just.minutes should be("1 minute")
    -1.render.duration.just.minutes should be("-1 minutes")

    1.render.duration.just.hours should be("1 hour")
    -1.render.duration.just.hours should be("-1 hours")

    1.render.duration.just.days should be("1 day")
    -1.render.duration.just.days should be("-1 days")

    1.render.duration.just.weeks should be("1 week")
    -1.render.duration.just.weeks should be("-1 weeks")

    1.render.duration.just.months should be("1 month")
    -1.render.duration.just.months should be("-1 months")

    1.render.duration.just.years should be("1 year")
    -1.render.duration.just.years should be("-1 years")
  }

  test("""It should be possible to render multiple durations""") {
    2.render.duration.just.nanoseconds should be("2 nanoseconds")
    -2.render.duration.just.nanoseconds should be("-2 nanoseconds")

    2.render.duration.just.milliseconds should be("2 milliseconds")
    -2.render.duration.just.milliseconds should be("-2 milliseconds")

    2.render.duration.just.seconds should be("2 seconds")
    -2.render.duration.just.seconds should be("-2 seconds")

    2.render.duration.just.minutes should be("2 minutes")
    -2.render.duration.just.minutes should be("-2 minutes")

    2.render.duration.just.hours should be("2 hours")
    -2.render.duration.just.hours should be("-2 hours")

    2.render.duration.just.days should be("2 days")
    -2.render.duration.just.days should be("-2 days")

    2.render.duration.just.weeks should be("2 weeks")
    -2.render.duration.just.weeks should be("-2 weeks")

    2.render.duration.just.months should be("2 months")
    -2.render.duration.just.months should be("-2 months")

    2.render.duration.just.years should be("2 years")
    -2.render.duration.just.years should be("-2 years")
  }

  test("""It should be possible to render human readble durations from months""") {
    0.render.duration.months should be("0 months")
    1.render.duration.months should be("1 month")
    -1.render.duration.months should be("-1 months")

    val upperBound = 12

    (-over(upperBound)).render.duration.months should be("-1 years 1 month")
    (-under(upperBound)).render.duration.months should be("-11 months")
    under(upperBound).render.duration.months should be("11 months")
    upperBound.render.duration.months should be("1 year")
    over(upperBound).render.duration.months should be("1 year 1 month")

    under(upperBound * 2).render.duration.months should be("1 year 11 months")
    (upperBound * 2).render.duration.months should be("2 years")
    over(upperBound * 2).render.duration.months should be("2 years 1 month")
  }

  test("""It should be possible to render human readble durations from days""") {
    0.render.duration.days should be("0 days")
    1.render.duration.days should be("1 day")
    -1.render.duration.days should be("-1 days")

    val upperBound = 7

    (-over(upperBound)).render.duration.days should be("-1 weeks 1 day")
    (-under(upperBound)).render.duration.days should be("-6 days")
    under(upperBound).render.duration.days should be("6 days")
    upperBound.render.duration.days should be("1 week")
    over(upperBound).render.duration.days should be("1 week 1 day")

    under(upperBound * 2).render.duration.days should be("1 week 6 days")
    (upperBound * 2).render.duration.days should be("2 weeks")
    over(upperBound * 2).render.duration.days should be("2 weeks 1 day")
  }

  test("""It should be possible to render human readble durations from hours""") {
    0.render.duration.hours should be("0 hours")
    1.render.duration.hours should be("1 hour")
    -1.render.duration.hours should be("-1 hours")

    val upperBound = 24

    (-over(upperBound)).render.duration.hours should be("-1 days 1 hour")
    (-under(upperBound)).render.duration.hours should be("-23 hours")
    under(upperBound).render.duration.hours should be("23 hours")
    upperBound.render.duration.hours should be("1 day")
    over(upperBound).render.duration.hours should be("1 day 1 hour")

    under(upperBound * 2).render.duration.hours should be("1 day 23 hours")
    (upperBound * 2).render.duration.hours should be("2 days")
    over(upperBound * 2).render.duration.hours should be("2 days 1 hour")
  }

  test("""It should be possible to render human readble durations from minutes""") {
    0.render.duration.minutes should be("0 minutes")
    1.render.duration.minutes should be("1 minute")

    val upperBound = 60

    (-over(upperBound)).render.duration.minutes should be("-1 hours 1 minute")
    (-under(upperBound)).render.duration.minutes should be("-59 minutes")
    under(upperBound).render.duration.minutes should be("59 minutes")
    upperBound.render.duration.minutes should be("1 hour")
    over(upperBound).render.duration.minutes should be("1 hour 1 minute")

    under(upperBound * 2).render.duration.minutes should be("1 hour 59 minutes")
    (upperBound * 2).render.duration.minutes should be("2 hours")
    over(upperBound * 2).render.duration.minutes should be("2 hours 1 minute")

    val previousUpperBound = 24
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.minutes should be("1 day 59 minutes")
    upperBoundPower.render.duration.minutes should be("1 day 1 hour")
    over(upperBoundPower).render.duration.minutes should be("1 day 1 hour 1 minute")
  }

  test("""It should be possible to render human readble durations from seconds""") {
    0.render.duration.seconds should be("0 seconds")
    1.render.duration.seconds should be("1 second")

    val upperBound = 60

    (-under(upperBound)).render.duration.seconds should be("-59 seconds")
    (-over(upperBound)).render.duration.seconds should be("-1 minutes 1 second")
    under(upperBound).render.duration.seconds should be("59 seconds")
    upperBound.render.duration.seconds should be("1 minute")
    over(upperBound).render.duration.seconds should be("1 minute 1 second")

    under(upperBound * 2).render.duration.seconds should be("1 minute 59 seconds")
    (upperBound * 2).render.duration.seconds should be("2 minutes")
    over(upperBound * 2).render.duration.seconds should be("2 minutes 1 second")

    val previousUpperBound = 60
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.seconds should be("1 hour 59 seconds")
    upperBoundPower.render.duration.seconds should be("1 hour 1 minute")
    over(upperBoundPower).render.duration.seconds should be("1 hour 1 minute 1 second")

    val previousUpperBoundPower = 24
    val upperBoundPowerPower = upperBound * previousUpperBound * previousUpperBoundPower + upperBoundPower

    under(upperBoundPowerPower).render.duration.seconds should be("1 day 1 hour 59 seconds")
    upperBoundPowerPower.render.duration.seconds should be("1 day 1 hour 1 minute")
    over(upperBoundPowerPower).render.duration.seconds should be("1 day 1 hour 1 minute 1 second")
  }

  test("""It should be possible to render human readble durations from nanoseconds""") {
    0.render.duration.nanoseconds should be("0 nanoseconds")
    1.render.duration.nanoseconds should be("1 nanosecond")

    val upperBound = 1000000

    (-under(upperBound)).render.duration.nanoseconds should be("-999999 nanoseconds")
    (-over(upperBound)).render.duration.nanoseconds should be("-1 milliseconds 1 nanosecond")
    under(upperBound).render.duration.nanoseconds should be("999999 nanoseconds")
    upperBound.render.duration.nanoseconds should be("1 millisecond")
    over(upperBound).render.duration.nanoseconds should be("1 millisecond 1 nanosecond")

    under(upperBound * 2).render.duration.nanoseconds should be("1 millisecond 999999 nanoseconds")
    (upperBound * 2).render.duration.nanoseconds should be("2 milliseconds")
    over(upperBound * 2).render.duration.nanoseconds should be("2 milliseconds 1 nanosecond")

    val previousUpperBound = 1000
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.nanoseconds should be("1 second 999999 nanoseconds")
    upperBoundPower.render.duration.nanoseconds should be("1 second 1 millisecond")
    over(upperBoundPower).render.duration.nanoseconds should be("1 second 1 millisecond 1 nanosecond")
  }

  test("""Long.MinValue should not cause any problems""") {
    Long.MinValue.render.duration.months should be("-768614336404564650 years 8 months")
    Long.MinValue.render.duration.days should be("-1317624576693539401 weeks 1 day")
    Long.MinValue.render.duration.hours should be("-54901024028897475 weeks 8 hours")
    Long.MinValue.render.duration.minutes should be("-915017067148291 weeks 1 day 18 hours 8 minutes")
    Long.MinValue.render.duration.seconds should be("-15250284452471 weeks 3 days 15 hours 30 minutes 8 seconds")
    Long.MinValue.render.duration.milliseconds should be("-15250284452 weeks 3 days 7 hours 12 minutes 55 seconds 808 milliseconds")
    Long.MinValue.render.duration.nanoseconds should be("-15250 weeks 1 day 23 hours 47 minutes 16 seconds 854 milliseconds 775808 nanoseconds")
  }

  private def under(bound: Long): Long = bound - 1
  private def over(bound: Long): Long = bound + 1
}
