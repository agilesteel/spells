package spells.user

private[user] object TestSamples {
  val samples =
    Vector[String](
      "week default",
      null,
      "green".green,
      styled(s"""yellow${"cyan".cyan}yellow""")(AnsiStyle.Yellow)
    )
}
