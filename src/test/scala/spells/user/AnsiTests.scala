package spells.user

class AnsiTests extends UnitTestConfiguration {
  test("""73.green should be(Green.value + "73" + Reset.value)""") {
    73.green should be(Green.value + "73" + Reset.value)
  }

  test(""""style".toAnsiStyle should be(spells.Ansi.Style("style"))""") {
    "style".toAnsiStyle should be(new spells.Ansi.Style("style"))
  }

  test(""""green" in customStyle should be(customStyle.value + "green" + Reset.value)""") {
    val customStyle = "style".toAnsiStyle
    "green" in customStyle should be(customStyle.value + "green" + Reset.value)
  }

  test("""The toAnsiStyle method should handle human capitalised readable colors""") {
    "Untouched".toAnsiStyle should be(Reset)
    "Reset".toAnsiStyle should be(Reset)
    "Black".toAnsiStyle should be(Black)
    "Red".toAnsiStyle should be(Red)
    "Green".toAnsiStyle should be(Green)
    "Yellow".toAnsiStyle should be(Yellow)
    "Blue".toAnsiStyle should be(Blue)
    "Magenta".toAnsiStyle should be(Magenta)
    "Cyan".toAnsiStyle should be(Cyan)
    "White".toAnsiStyle should be(White)
    "Bold".toAnsiStyle should be(Bold)
    "Blink".toAnsiStyle should be(Blink)
    "Reversed".toAnsiStyle should be(Reversed)
    "Invisible".toAnsiStyle should be(Invisible)
  }

  test(""""green".green should be(Green.value + "green" + Reset.value)""") {
    "green".green should be(Green.value + "green" + Reset.value)
  }

  test(""""green".yellow.red.green should be("green".green)""") {
    "green".yellow.red.green should be("green".green)
  }

  test("""Green.toString should be("sample" in Green)""") {
    Green.toString should be("sample" in Green)
  }

  test("""Scoverage should be happy""") {
    val styles =
      Vector(
        Black,
        Red,
        Green,
        Yellow,
        Blue,
        Magenta,
        Cyan,
        White,
        Bold,
        Blink,
        Reversed,
        Invisible
      )

    val factories =
      Vector[Any => String](
        _.black,
        _.red,
        _.green,
        _.yellow,
        _.blue,
        _.magenta,
        _.cyan,
        _.white,
        _.bold,
        _.blink,
        _.reversed,
        _.invisible
      )

    forEvery(styles zip factories) {
      case (style, factory) =>
        "sample" in style should be(style.value + "sample" + Reset.value)
        factory("sample") should be("sample" in style)
    }
  }
}
