package spells

/** Contains utility methods for `Throwable`s. */
trait ThrowableOpsModule {
  this: AnsiModule
    with CustomRenderingModule
    with SpellsConfigModule
    with AnsiModule
    with StringOpsModule
    with StylePrintModule =>

  import java.io.{PrintWriter, StringWriter}

  final implicit class ThrowableOpsFromSpells(value: Throwable)
      extends CustomRendering {
    final override def rendered(
        implicit
        availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String =
      styled(getFullStackTraceString)(AnsiStyle.Red)

    final def getFullStackTraceString: String = {
      val writer = new StringWriter
      val autoFlush = true
      value.printStackTrace(new PrintWriter(writer, autoFlush))
      writer.getBuffer
        .deleteCharAt(writer.getBuffer.length - 1)
        .toString
        .replace("\t", "     ")
    }

    /** Gets the deepest `Throwable`.
      * @return the root cause
      */
    @scala.annotation.tailrec
    final def getRootCause: Throwable =
      if (value.getCause == null) value
      else value.getCause.getRootCause
  }
}
