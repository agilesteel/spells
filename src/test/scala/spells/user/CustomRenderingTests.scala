package spells.user

class CustomRenderingTests extends UnitTestConfiguration {
  test("It should not be possible to implicitly pass in availableWidthInCharacters of type Int for rendered") {
    implicit val availableWidthInCharacters: Int = 1337

    CR.rendered should be(spells.CustomRendering.Defaults.AvailableWidthInCharacters.toString)
  }

  test("It should be possible to explicitly pass in availableWidthInCharacters of type Int for rendered") {
    val availableWidthInCharacters: Int = 1337

    CR.rendered(availableWidthInCharacters) should be(availableWidthInCharacters.toString)
  }

  object CR extends spells.CustomRendering {
    def rendered(implicit availableWidthInCharacters: spells.CustomRendering.AvailableWidthInCharacters = spells.CustomRendering.Defaults.AvailableWidthInCharacters.value): String =
      availableWidthInCharacters.toString
  }
}
