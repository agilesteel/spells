package compilation

class CompilationTests extends spells.UnitTestConfiguration {
  test("All these examples should compile") {
    "this should compile".green
    cleared("this should compile")(AnsiStyle.Green)
    erred("this should compile")
    styled("this should compile")(AnsiStyle.Green)

    def weak[T](t: T): T = t.xrayWeak // no TypeTag

    spells.user.SilentOutputStream out {
      10.xray("description")
      10.xrayIf(_ => true)
      10.xrayIf(_ => false)

      10.xrayWeak("description")
      10.xrayIfWeak(_ => true)
      10.xrayIfWeak(_ => false)
    }

    Array.empty[Any].rendered
    List.empty[Any].rendered
    Seq.empty[Any].rendered
    Map.empty[Any, Any].rendered
    new java.util.HashSet[Any].rendered
    new java.util.HashMap[Any, Any].rendered

    object CR extends CustomRendering {
      override final def rendered(implicit availableWidthInCharacters: spells.StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String = ???
    }

    "".wrappedOnSpaces
  }
}
