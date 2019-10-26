package spells.user

class CustomRenderingTests extends spells.UnitTestConfiguration {
  test(
    "It should not be possible to implicitly pass in availableWidthInCharacters of type Int for rendered"
  ) {
    implicit val availableWidthInCharacters: Int = 1337

    CR.rendered should be(
      SpellsConfig.terminal.WidthInCharacters.value.toString
    )
  }

  test(
    "It should be possible to explicitly pass in availableWidthInCharacters of type Int for rendered"
  ) {
    val availableWidthInCharacters: Int = 1337

    CR.rendered(availableWidthInCharacters) should be(
      availableWidthInCharacters.toString
    )
  }

  object CR extends CustomRendering {
    final override def rendered(
        implicit
        availableWidthInCharacters: spells.StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String =
      availableWidthInCharacters.toString
  }
}
