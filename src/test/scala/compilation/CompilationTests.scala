package compilation

class CompilationTests extends spells.UnitTestConfiguration {
  test("All these examples should compile") {
    "this should compile".green
    cleared("this should compile")(Green)
    erred("this should compile")
    styled("this should compile")(Green)

    spells.user.SilentOutputStream out {
      10.xray("description")
    }

    Array.empty[Any].rendered
    List.empty[Any].rendered
    Seq.empty[Any].rendered
    Map.empty[Any, Any].rendered
    new java.util.HashSet[Any].rendered
    new java.util.HashMap[Any, Any].rendered

    object CR extends CustomRendering {
      override def rendered(implicit availableWidthInCharacters: spells.CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = ???
    }
  }
}
