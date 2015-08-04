package spells.user

class StringOpsTests extends UnitTestConfiguration {
  test("withDecodedScalaSymbols should delegate to scala.reflect.NameTransformer.decode") {
    val sample = "Encoded + Whatever"
    val encodedSample = scala.reflect.NameTransformer encode sample

    encodedSample.withDecodedScalaSymbols should be(sample)
  }

  val Limit = 10

  test("Worst case scenario: the word is longer than the limit: the word should NOT be broken") {
    "helloworld!".wrappedOnSpaces(Limit) should be("helloworld!")
  }

  test("Empty should stay empty") {
    "".wrappedOnSpaces(Limit) should be('empty)
  }

  test("A line should be broken at the breakCharacter before the Limit") {
    "hello world".wrappedOnSpaces(Limit) should be("hello\nworld")
  }

  test("A line should be broken at the latest breakCharacter possible") {
    "hi world, you are awesome".wrappedOnSpaces(Limit) should be("hi world,\nyou are\nawesome")
  }

  test("Worst case scenario 2: the word that's longer than the limit is followed by another word") {
    "helloworld! next".wrappedOnSpaces(Limit) should be("helloworld!\nnext")
  }

  test("Multiple spaces") {
    "Saturday, March 7, 2015 11:06:53 AM CET".wrappedOnSpaces(160) should be("Saturday, March 7, 2015 11:06:53 AM CET")
  }

  test("Wrap on a 'per line' basis") {
    "xxxx \nxxxx".wrappedOnSpaces(4) should be("xxxx\nxxxx")
  }
}
