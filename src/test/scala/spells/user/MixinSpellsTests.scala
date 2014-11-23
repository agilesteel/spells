package spells.user

class MixinSpellsTests extends UnitTestConfiguration {
  test("Mixin spells into package object instead of importing it should be possible") {
    "this should compile".green
    cleared("this should compile")(Green)
    erred("this should compile")
    styled("this should compile")(Green)
  }
}
