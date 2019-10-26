package spells.user

class StackTraceElementTests extends spells.UnitTestConfiguration {
  test("Full StackTraceElement rendering") {
    val nativeMethod = -2

    val elements = Set(
      new StackTraceElement("declaringClass", "methodName", "fileName", 0),
      new StackTraceElement("declaringClass", "methodName", "fileName", -1),
      new StackTraceElement(
        "declaringClass",
        "methodName",
        "fileName",
        nativeMethod
      ),
      new StackTraceElement("declaringClass", "methodName", null, 0),
      new StackTraceElement("declaringClass", "methodName", null, -1),
      new StackTraceElement("declaringClass", "methodName", null, nativeMethod)
    )

    forEvery(elements)(renderedShouldBeToString)
  }

  private def renderedShouldBeToString(element: StackTraceElement) =
    element.rendered should be(element.toString)
}
