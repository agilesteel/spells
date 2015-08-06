package compilation

class Compilation extends spells.user.UnitTestConfiguration {
  test("Xray.Description implicit should compile") {
    10.xray("description", monitor = noop)
  }
}
