package spells.user

import AnsiStyle._

class AnsiTests extends spells.UnitTestConfiguration {
  test("""73.green should be(Green.value + "73" + Reset.value)""") {
    73.green should be(Green.value + "73" + Reset.value)
  }

  test(""""style".toAnsiStyle.value should be(AnsiStyle("style").value)""") {
    "style".toAnsiStyle.value should be(new AnsiStyle("style").value)
  }

  test("""The null.toAnsiStyle call should yield an empty AnsiStyle """) {
    (null: String).toAnsiStyle.value should be(new AnsiStyle("").value)
  }

  test(""""green" in customStyle should be(customStyle.value + "green" + Reset.value)""") {
    val customStyle = "style".toAnsiStyle
    "green" in customStyle should be(customStyle.value + "green" + Reset.value)
  }

  test("""The toAnsiStyle method should handle human capitalised readable colors""") {
    "Untouched".toAnsiStyle.value should be(Reset.value)
    "Black".toAnsiStyle.value should be(Black.value)
    "Red".toAnsiStyle.value should be(Red.value)
    "Green".toAnsiStyle.value should be(Green.value)
    "Yellow".toAnsiStyle.value should be(Yellow.value)
    "Blue".toAnsiStyle.value should be(Blue.value)
    "Magenta".toAnsiStyle.value should be(Magenta.value)
    "Cyan".toAnsiStyle.value should be(Cyan.value)
    "White".toAnsiStyle.value should be(White.value)
    AnsiStyle.All should contain(AnsiStyle.Random)
    AnsiStyle.All should contain("Random".toAnsiStyle)
    Green.value.toAnsiStyle.value should be(Green.value)
  }

  test(""""green".green should be(Green.value + "green" + Reset.value)""") {
    "green".green should be(Green.value + "green" + Reset.value)
  }

  test(""""green".yellow.red.green should be("green".green)""") {
    "green".yellow.red.green should be("green".green)
  }

  test("""Green.toString should be("sample" in Green)""") {
    Black.toString should be("Black" in Black)
    Red.toString should be("Red" in Red)
    Green.toString should be("Green" in Green)
    Yellow.toString should be("Yellow" in Yellow)
    Blue.toString should be("Blue" in Blue)
    Magenta.toString should be("Magenta" in Magenta)
    Cyan.toString should be("Cyan" in Cyan)
    White.toString should be("White" in White)
    "style".toAnsiStyle.toString should be("style" + "sample" + Reset.value)
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
        White)

    val factories =
      Vector[Any => String](
        _.black,
        _.red,
        _.green,
        _.yellow,
        _.blue,
        _.magenta,
        _.cyan,
        _.white)

    forEvery(styles zip factories) {
      case (style, factory) =>
        "sample" in style should be(style.value + "sample" + Reset.value)
        factory("sample") should be("sample" in style)
    }
  }
}
