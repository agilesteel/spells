package spells.user

class AnsiTests extends spells.UnitTestConfiguration {
  test("""73.green should be(Green.value + "73" + Reset.value)""") {
    73.green should be(Green.value + "73" + Reset.value)
  }

  test(""""style".toAnsiStyle should be(spells.Ansi.Style("style"))""") {
    "style".toAnsiStyle should be(new spells.Ansi.Style("style"))
  }

  test("""An IllegalArgumentException should be thrownBy (null: String).toAnsiStyle""") {
    an[IllegalArgumentException] should be thrownBy {
      (null: String).toAnsiStyle
    }
  }

  test(""""green" in customStyle should be(customStyle.value + "green" + Reset.value)""") {
    val customStyle = "style".toAnsiStyle
    "green" in customStyle should be(customStyle.value + "green" + Reset.value)
  }

  test("""The toAnsiStyle method should handle human capitalised readable colors""") {
    "Untouched".toAnsiStyle.value should be(Reset.value)
    "Reset".toAnsiStyle.value should be(Reset.value)
    "Black".toAnsiStyle.value should be(Black.value)
    "Red".toAnsiStyle.value should be(Red.value)
    "Green".toAnsiStyle.value should be(Green.value)
    "Yellow".toAnsiStyle.value should be(Yellow.value)
    "Blue".toAnsiStyle.value should be(Blue.value)
    "Magenta".toAnsiStyle.value should be(Magenta.value)
    "Cyan".toAnsiStyle.value should be(Cyan.value)
    "White".toAnsiStyle.value should be(White.value)
    spells.Ansi.AllStylesOutOfTheBox should contain(spells.Ansi.Random)
    spells.Ansi.AllStylesOutOfTheBox should contain("Random".toAnsiStyle)
    Green.value.toAnsiStyle.value should be(Green.value)
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
        White
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
        _.white
      )

    forEvery(styles zip factories) {
      case (style, factory) =>
        "sample" in style should be(style.value + "sample" + Reset.value)
        factory("sample") should be("sample" in style)
    }
  }
}
