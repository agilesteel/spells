package spells.user

class StylePrintTests extends UnitTestConfiguration {
  test("""styled("white") should be("white")""") {
    styled("white") should be("white")
  }

  test("""styled("yellow")(Yellow) should startWith(Yellow.value)""") {
    styled("yellow")(Yellow) should startWith(Yellow.value)
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

  // TODO: Test with https://www.scalacheck.org/
  test("""Line breaks of any kind should not cause any trouble""") {
    styled("yellow\r\n" + "green".green)(Yellow) should be("yellow\r\n".yellow + "green".green)
    styled("yellow\n" + "green".green)(Yellow) should be("yellow\n".yellow + "green".green)
    styled("yellow\r" + "green".green + "yellow")(Yellow) should be("yellow\r".yellow + "green".green + "yellow".yellow)
    styled("yellow\n" + "green".green + "yellow")(Yellow) should be("yellow\n".yellow + "green".green + "yellow".yellow)
    styled("\ryellow" + "green".green + "yellow")(Yellow) should be("\ryellow".yellow + "green".green + "yellow".yellow)
    styled("\nyellow" + "green".green + "yellow")(Yellow) should be("\nyellow".yellow + "green".green + "yellow".yellow)
    styled("\r\r\nyellow" + "green".green + "\n\ryellow")(Yellow) should be("\r\r\nyellow".yellow + "green".green + "\n\ryellow".yellow)
  }

  test("""styled("green\ngreen".green + "yellow")(Yellow) should be("green\ngreen".green + "yellow".yellow)""") {
    styled("green\ngreen".green + "yellow")(Yellow) should be("green\ngreen".green + "yellow".yellow)
  }

  test("""styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)""") {
    implicit val customStyle = Yellow
    styled("yellow" + "red".red + "yellow" + "green".green + "yellow") should be("yellow".yellow + "red".red + "yellow".yellow + "green".green + "yellow".yellow)
  }

  test("""Scoverage should be happy""") {
    SilentOutputStream out {
      print()
      println()
    }
  }
}
