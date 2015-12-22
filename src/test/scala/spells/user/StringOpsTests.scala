package spells.user

class StringOpsTests extends spells.UnitTestConfiguration {
  test("The withDecodedScalaSymbols method should delegate to scala.reflect.NameTransformer.decode") {
    val sample = "Encoded + Whatever"
    val encodedSample = scala.reflect.NameTransformer encode sample

    encodedSample.withDecodedScalaSymbols should be(sample)
  }

  val Limit = 10

  test("Worst case scenario: the word is longer than the limit: the word should NOT be broken") {
    "helloworld!".wrappedOnSpaces(Limit) should be("helloworld!")
  }

  test("Dodgy strings should not cause any trouble") {
    "".wrappedOnSpaces(Limit) should be('empty)
    " ".wrappedOnSpaces(Limit) should be(" ")
    "\n".wrappedOnSpaces(Limit) should be("\n")
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

  test("Wrapping with multispace should preserve spaces in the end, but ignore them when calculating the length of the line") {
    "x  y".wrappedOnSpaces(1) should be("x \ny")
  }

  test("Wrap on a 'per line' basis") {
    "xxxx \nxxxx".wrappedOnSpaces(4) should be("xxxx\nxxxx")
  }

  test("Wrap on a 'per line' basis2") {
    "xxxx\n xxx".wrappedOnSpaces(4) should be("xxxx\n xxx")
  }

  test("Wrap should preserve empty lines") {
    "xxxx\n\n xxx".wrappedOnSpaces(4) should be("xxxx\n\n xxx")
  }

  test("Wrap should preserve empty lines2") {
    "xxxx\n\n\n xxx".wrappedOnSpaces(4) should be("xxxx\n\n\n xxx")
  }
}
