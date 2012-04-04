package spells

import Spells._

class AnsiPrintTests extends UnitTestConfiguration {
  test(""""green".green should be(Green + "green" + Reset)""") {
    "green".green should be(Green + "green" + Reset)
  }

  test(""""green".yellow.red.green should be("green".green)""") {
    "green".yellow.red.green should be("green".green)
  }

  test("""styled("white") should be("white")""") {
    styled("white") should be("white")
  }

  test("""styled("green")(Green) should be("green".green)""") {
    styled("green")(Green) should be("green".green)
  }

  test("""styled("green".green) should be("green".green)""") {
    styled("green".green) should be("green".green)
  }

  test("""styled("green".magenta)(Green) should be("green".green)""") {
    styled("green".magenta)(Green) should be("green".green)
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

  test("""styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be(Yellow + "yellow" + Green + "green" + "yellow".yellow)""") {
    styled("yellow" + "green".green + "yellow".yellow)(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow".yellow + "green".green + "yellow")(Yellow) should be("yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow")(Yellow) should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow") with implicit DefaultStyle = Yellow should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    implicit val DefaultStyle = Yellow
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
  }
}