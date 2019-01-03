package spells.user

class FunctionNTests extends spells.UnitTestConfiguration {
  test("FunctionN rendering tests") {
    (() => "").rendered should include regex """\(\) => .*String"""
    ((_: Int) => "").rendered should include regex """Int => .*String"""
    ((_: Int, _: Int) => "").rendered should include regex """\(Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should include regex """\(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int\) => .*String"""
  }
}
