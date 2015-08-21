package compilation

class CompilationTests extends spells.user.UnitTestConfiguration {
  test("Xray.Description implicit should compile") {
    spells.user.SilentOutputStream out {
      10.xray("description")
    }

    Array.empty[Any].rendered
    List.empty[Any].rendered
    Seq.empty[Any].rendered
    Map.empty[Any, Any].rendered
  }
}
