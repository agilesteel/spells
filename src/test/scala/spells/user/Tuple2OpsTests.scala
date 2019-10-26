package spells.user

class Tuple2OpsTests extends spells.UnitTestConfiguration {
  test("Tuple2 should always be rendered with an arrow") {
    (1 -> "II").rendered should be("1 -> II")
  }

  test("Recursive rendering should work out of the box") {
    val now = java.util.Calendar.getInstance
    (now -> now).rendered should be(s"${now.rendered} -> ${now.rendered}")
  }

  test("Do not fiddle with anything if simple rendering fits into one line") {
    val availableWidthInCharacters =
      SpellsConfig.terminal.WidthInCharacters.value
    val separatorWithSpacesWidthInCharacters = 4
    val equalSize = (availableWidthInCharacters / 2) - (separatorWithSpacesWidthInCharacters / 2)
    val key = "x" * equalSize
    val value = "y" * equalSize

    (key.cyan -> value.blue).rendered should be(s"${key.cyan} -> ${value.blue}")
  }

  test(
    "Multiline value should cause the value to start on the next line after the key. The separator should always stay on the last line of the key. Value is padded with 2 white spaces"
  ) {
    val key = "key"
    val value = "first\nsecond"

    (key.cyan -> value.blue).rendered should be {
      s"${key.cyan} ->" + "\n" +
        s"  ${AnsiStyle.Blue.value}first" + "\n" +
        "  second" + AnsiStyle.Reset.value
    }
  }

  test(
    "Multiline value should cause the value to start on the next line after the key. The separator should always stay on the last line of the key even if the key has to be rewrapped. Value is padded with 2 white spaces"
  ) {
    val availableWidthInCharacters =
      SpellsConfig.terminal.WidthInCharacters.value
    val separatorWidthIfItIsOnLastLine = 3
    val firstHalf = "x " * (availableWidthInCharacters / 2)
    val secondHalf = "y " * (availableWidthInCharacters / 2)
    val key = firstHalf + secondHalf
    val value = "first\nsecond"

    (key.cyan -> value.blue).rendered should be {
      AnsiStyle.Cyan.value + firstHalf.trim + "\n" +
        secondHalf + AnsiStyle.Reset.value + "\n" +
        "->" + "\n" +
        s"  ${AnsiStyle.Blue.value}first" + "\n" +
        "  second" + AnsiStyle.Reset.value
    }
  }

  test(
    "Singleline value should be added to the same line as the last line of key if it fits"
  ) {
    val availableWidthInCharacters = 15
    val key = "first\nsecond"
    val value = "value"

    (key.cyan -> value.blue).rendered(availableWidthInCharacters) should be {
      key.cyan + " -> " + value.blue
    }
  }

  test(
    "Singleline value should be treated as multiline if it does not fir into the last line of the key"
  ) {
    val availableWidthInCharacters = 15
    val key = "first\nsecond"
    val value = "valueTOOMUCH"

    (key.cyan -> value.blue).rendered(availableWidthInCharacters) should be {
      key.cyan + " ->" + "\n" +
        "  " + value.blue
    }
  }

  test("Value should be rewrapped in case of complex rendering") {
    val availableWidthInCharacters = 15
    val key = "first\nsecond"
    val value = "fivecfivecfiv c " + "fivecfivecfiv c"

    (key.cyan -> value.blue).rendered(availableWidthInCharacters) should be {
      key.cyan + " ->" + "\n" +
        "  " + AnsiStyle.Blue.value + "fivecfivecfiv" + "\n" +
        "  " + "c" + "\n" +
        "  " + "fivecfivecfiv" + "\n" +
        "  " + "c" + AnsiStyle.Reset.value
    }
  }
}
