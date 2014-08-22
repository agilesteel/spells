package spells

class HumanRenderingTests extends UnitTestConfiguration {
  test("""It should be possible to call render on an int, long""") {
    1.render should be(Rendering(1))
    1l.render should be(Rendering(1))
  }

  test("""It should be possible to render a single duration""") {
    -1.render.duration.nanoseconds should be("-1 nanosecond")
    1.render.duration.nanoseconds should be("1 nanosecond")

    1.render.duration.milliseconds should be("1 millisecond")
    -1.render.duration.milliseconds should be("-1 millisecond")

    1.render.duration.seconds should be("1 second")
    -1.render.duration.seconds should be("-1 second")

    1.render.duration.minutes should be("1 minute")
    -1.render.duration.minutes should be("-1 minute")

    1.render.duration.hours should be("1 hour")
    -1.render.duration.hours should be("-1 hour")

    1.render.duration.days should be("1 day")
    -1.render.duration.days should be("-1 day")

    1.render.duration.years should be("1 year")
    -1.render.duration.years should be("-1 year")
  }

  test("""It should be possible to render multiple durations""") {
    2.render.duration.nanoseconds should be("2 nanoseconds")
    -2.render.duration.nanoseconds should be("-2 nanoseconds")

    2.render.duration.milliseconds should be("2 milliseconds")
    -2.render.duration.milliseconds should be("-2 milliseconds")

    2.render.duration.seconds should be("2 seconds")
    -2.render.duration.seconds should be("-2 seconds")

    2.render.duration.minutes should be("2 minutes")
    -2.render.duration.minutes should be("-2 minutes")

    2.render.duration.hours should be("2 hours")
    -2.render.duration.hours should be("-2 hours")

    2.render.duration.days should be("2 days")
    -2.render.duration.days should be("-2 days")

    2.render.duration.years should be("2 years")
    -2.render.duration.years should be("-2 years")
  }

  test("""It should be possible to render human readble durations from hours""") {
    0.render.duration.from.hours should be("0 hours")
    1.render.duration.from.hours should be("1 hour")
    -1.render.duration.from.hours should be("-1 hour")

    val upperBound = 24

    (-over(upperBound)).render.duration.from.hours should be("-1 day 1 hour")
    (-under(upperBound)).render.duration.from.hours should be("-23 hours")
    under(upperBound).render.duration.from.hours should be("23 hours")
    upperBound.render.duration.from.hours should be("1 day")
    over(upperBound).render.duration.from.hours should be("1 day 1 hour")

    under(upperBound * 2).render.duration.from.hours should be("1 day 23 hours")
    (upperBound * 2).render.duration.from.hours should be("2 days")
    over(upperBound * 2).render.duration.from.hours should be("2 days 1 hour")
  }

  test("""It should be possible to render human readble durations from minutes""") {
    0.render.duration.from.minutes should be("0 minutes")
    1.render.duration.from.minutes should be("1 minute")

    val upperBound = 60

    (-over(upperBound)).render.duration.from.minutes should be("-1 hour 1 minute")
    (-under(upperBound)).render.duration.from.minutes should be("-59 minutes")
    under(upperBound).render.duration.from.minutes should be("59 minutes")
    upperBound.render.duration.from.minutes should be("1 hour")
    over(upperBound).render.duration.from.minutes should be("1 hour 1 minute")

    under(upperBound * 2).render.duration.from.minutes should be("1 hour 59 minutes")
    (upperBound * 2).render.duration.from.minutes should be("2 hours")
    over(upperBound * 2).render.duration.from.minutes should be("2 hours 1 minute")

    val previousUpperBound = 24
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.from.minutes should be("1 day 59 minutes")
    upperBoundPower.render.duration.from.minutes should be("1 day 1 hour")
    over(upperBoundPower).render.duration.from.minutes should be("1 day 1 hour 1 minute")
  }

  test("""It should be possible to render human readble durations from seconds""") {
    0.render.duration.from.seconds should be("0 seconds")
    1.render.duration.from.seconds should be("1 second")

    val upperBound = 60

    (-under(upperBound)).render.duration.from.seconds should be("-59 seconds")
    (-over(upperBound)).render.duration.from.seconds should be("-1 minute 1 second")
    under(upperBound).render.duration.from.seconds should be("59 seconds")
    upperBound.render.duration.from.seconds should be("1 minute")
    over(upperBound).render.duration.from.seconds should be("1 minute 1 second")

    under(upperBound * 2).render.duration.from.seconds should be("1 minute 59 seconds")
    (upperBound * 2).render.duration.from.seconds should be("2 minutes")
    over(upperBound * 2).render.duration.from.seconds should be("2 minutes 1 second")

    val previousUpperBound = 60
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.from.seconds should be("1 hour 59 seconds")
    upperBoundPower.render.duration.from.seconds should be("1 hour 1 minute")
    over(upperBoundPower).render.duration.from.seconds should be("1 hour 1 minute 1 second")

    val previousUpperBoundPower = 24
    val upperBoundPowerPower = upperBound * previousUpperBound * previousUpperBoundPower + upperBoundPower

    under(upperBoundPowerPower).render.duration.from.seconds should be("1 day 1 hour 59 seconds")
    upperBoundPowerPower.render.duration.from.seconds should be("1 day 1 hour 1 minute")
    over(upperBoundPowerPower).render.duration.from.seconds should be("1 day 1 hour 1 minute 1 second")
  }

  test("""It should be possible to render human readble durations from nanoseconds""") {
    0.render.duration.from.nanoseconds should be("0 nanoseconds")
    1.render.duration.from.nanoseconds should be("1 nanosecond")

    val upperBound = 1000000

    (-under(upperBound)).render.duration.from.nanoseconds should be("-999999 nanoseconds")
    (-over(upperBound)).render.duration.from.nanoseconds should be("-1 millisecond 1 nanosecond")
    under(upperBound).render.duration.from.nanoseconds should be("999999 nanoseconds")
    upperBound.render.duration.from.nanoseconds should be("1 millisecond")
    over(upperBound).render.duration.from.nanoseconds should be("1 millisecond 1 nanosecond")

    under(upperBound * 2).render.duration.from.nanoseconds should be("1 millisecond 999999 nanoseconds")
    (upperBound * 2).render.duration.from.nanoseconds should be("2 milliseconds")
    over(upperBound * 2).render.duration.from.nanoseconds should be("2 milliseconds 1 nanosecond")

    val previousUpperBound = 1000
    val upperBoundPower = upperBound * previousUpperBound + upperBound

    under(upperBoundPower).render.duration.from.nanoseconds should be("1 second 999999 nanoseconds")
    upperBoundPower.render.duration.from.nanoseconds should be("1 second 1 millisecond")
    over(upperBoundPower).render.duration.from.nanoseconds should be("1 second 1 millisecond 1 nanosecond")
  }

  private def under(bound: Long): Long = bound - 1
  private def over(bound: Long): Long = bound + 1
}
