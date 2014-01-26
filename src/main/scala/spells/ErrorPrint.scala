package spells

trait ErrorPrint {
  @inline final def printerr(error: Any): Unit = {
    Console.err println erred(error)
  }

  final def erred(error: Any): String = styled(error)(Red)
}
