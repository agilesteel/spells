package spells

class AnsiTests extends UnitTestConfiguration {
  val samples = Vector("white", "green".green, styled(s"""yellow${"cyan".cyan}yellow""")(Yellow))

  test("""73.green should be(Green.value + "73" + Reset.value)""") {
    73.green should be(Green.value + "73" + Reset.value)
  }

  test(""""style".toAnsiStyle should be(AnsiStyle("style"))""") {
    "style".toAnsiStyle should be(AnsiStyle("style"))
  }

  test(""""green" in customStyle should be(customStyle.value + "green" + Reset.value)""") {
    val customStyle = "style".toAnsiStyle
    "green" in customStyle should be(customStyle.value + "green" + Reset.value)
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

  test("""styled("white") should be("white")""") {
    styled("white") should be("white")
  }

  test("""styled("green")(Green) should be("green".green)""") {
    styled("green")(Green) should be("green".green)
  }

  test("""styled(null) should be("null")""") {
    styled(null) should be("null")
  }

  test("""styled((null: String).red) should be("null".red)""") {
    styled((null: String).red) should be("null".red)
  }

  test("""styled("green".green) should be("green".green)""") {
    styled("green".green) should be("green".green)
  }

  test("""styled("magenta".magenta)(Green) should be("magenta".magenta)""") {
    styled("magenta".magenta)(Green) should be("magenta".magenta)
  }

  test("""styled("yellow".yellow + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "green".green)(Yellow) should be("yellow".yellow + "green".green)""") {
    styled("yellow" + "green".green)(Yellow) should be("yellow".yellow + "green".green)
  }

  test("""styled("yellow".yellow + "green")(Green) should be("yellow".yellow + "green".green)""") {
    styled("yellow".yellow + "green")(Green) should be("yellow".yellow + "green".green)
  }

  test("""styled("yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow".yellow + "green" + "yellow".yellow)(Green) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green" + "yellow".yellow)(Green) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    implicit val customStyle = Yellow
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(Clear))(Blue))""") {
    forEvery(samples) { sample =>
      cleared(sample)(Blue) should be(styled(styled(s"\r$sample")(ClearExtras.Clear))(Blue))
    }
  }

  test("""erred(error) should be(styled(error)(Red))""") {
    forEvery(samples) { error =>
      erred(error) should be(styled(error)(Red))
    }
  }
}
