package spells.user

import java.io._

class ThrowableOpsTests extends spells.UnitTestConfiguration {
  test("The getRootCause method should fall back to itself if there is no cause") {
    val runtime = new RuntimeException
    (runtime: Throwable).getRootCause.isInstanceOf[RuntimeException] should be(true)
  }

  test("The getRootCause method should descend to the end") {
    val io =
      new IOException(
        new RuntimeException(
          new FileNotFoundException
        )
      )

    io.getRootCause.isInstanceOf[FileNotFoundException] should be(true)
  }

  test("The getFullStackTraceString should write what printStackTrace produces into a string") {
    val runtime = new RuntimeException
    runtime.rendered should be(styled(runtime.getFullStackTraceString)(AnsiStyle.Red))
  }
}
