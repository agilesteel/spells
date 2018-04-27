package spells.user

class FunctionNTests extends spells.UnitTestConfiguration {
  test("FunctionN rendering tests") {
    (() => "").rendered should be("() => java.lang.String")
    ((_: Int) => "").rendered should be("Int => java.lang.String")
    ((_: Int, _: Int) => "").rendered should be("(Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be("(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => java.lang.String")
  }
}
