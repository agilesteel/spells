package compilation

class Compilation extends spells.user.UnitTestConfiguration {
  test("Xray.Description implicit should compile") {
    10.xray("description", monitor = noop)
    Array.empty.rendered
    List.empty.rendered
    Seq.empty.rendered
    Map.empty.rendered
  }
}
