package spells

class AnsiPrintTests extends UnitTestConfiguration {
  test(""""green" in customStyle should be(customStyle + "green" + Reset)""") {
    val customStyle = "style".toAnsiStyle
    "green" in customStyle should be(customStyle + "green" + Reset)
  }

  test(""""style".s should be (AnsiStyle("style"))""") {
    "style".toAnsiStyle should be(AnsiStyle("style"))
  }

  test("""""style".s.toString should be("style")""") {
    "style".toAnsiStyle.toString should be("style")
  }

  test(""""green".green should be(Green + "green" + Reset)""") {
    "green".green should be(Green + "green" + Reset)
  }

  test("""73.green should be(Green + "73" + Reset)""") {
    73.green should be(Green + "73" + Reset)
  }

  test(""""green".yellow.red.green should be("green".green)""") {
    "green".yellow.red.green should be("green".green)
  }

  test("""styled("white") should be("white")""") {
    styled("white") should be("white")
    println("white")
  }

  test("""styled("green")(Green) should be("green".green)""") {
    styled("green")(Green) should be("green".green)
    println("green")(Green)
  }

  test("""styled("green".green) should be("green".green)""") {
    styled("green".green) should be("green".green)
    println("green".green)
  }

  test("""styled("magenta".magenta)(Green) should be("magenta".magenta)""") {
    styled("magenta".magenta)(Green) should be("magenta".magenta)
    println("magenta".magenta)(Green)
  }

  test("""styled("yellow".yellow + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
    println("yellow".yellow + "green".green + "yellow".yellow)(Yellow)
  }

  test("""styled("yellow" + "green".green)(Yellow) should be("yellow".yellow + "green".green)""") {
    styled("yellow" + "green".green)(Yellow) should be("yellow".yellow + "green".green)
    println("yellow" + "green".green)(Yellow)
  }

  test("""styled("yellow".yellow + "green")(Green) should be("yellow".yellow + "green".green)""") {
    styled("yellow".yellow + "green")(Green) should be("yellow".yellow + "green".green)
    println("yellow".yellow + "green")(Green)
  }

  test("""styled("yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
    println("yellow" + "green".green + "yellow")(Yellow)
  }

  test("""styled("yellow".yellow + "green" + "yellow".yellow)(Green) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green" + "yellow".yellow)(Green) should be("yellow".yellow + "green".green + "yellow".yellow)
    println("yellow".yellow + "green" + "yellow".yellow)(Green)
  }

  test("""styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be(Yellow + "yellow" + Green + "green" + "yellow".yellow)""") {
    styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
    println("yellow" + "green".green + "yellow".yellow)(Yellow)
  }

  test("""styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
    println("yellow".yellow + "green".green + "yellow")(Yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
    println("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow") with implicit DefaultStyle = Yellow should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    implicit val DefaultStyle: AnsiStyle = Yellow
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
    println("yellow" + "red".red + "yellow" + "green".green + "yellow")
  }
}