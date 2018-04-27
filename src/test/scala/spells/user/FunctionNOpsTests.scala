package spells.user

class FunctionNTests extends spells.UnitTestConfiguration {
  test("FunctionN rendering tests") {
    (() => "").rendered should be(s"() => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int) => "").rendered should be(s"Int => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int) => "").rendered should be(s"(Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
    ((_: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int, _: Int) => "").rendered should be(s"(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) => ${typeBasedOnScalaVersion("java.lang.String")}")
  }
}
