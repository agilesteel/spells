package spells

/** Contains utility methods for `Throwable`s. */
trait ThrowableOpsModule {
  this: CustomRenderingModule with SpellsConfigModule with StylePrintModule with AnsiModule =>

  import java.io.{ PrintWriter, StringWriter }

  implicit final class ThrowableOpsFromSpells(value: Throwable) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      styled(getFullStackTraceString)(AnsiStyle.Red)

    final def getFullStackTraceString: String = {
      val writer = new StringWriter
      val autoFlush = true
      value.printStackTrace(new PrintWriter(writer, autoFlush))
      writer.getBuffer.deleteCharAt(writer.getBuffer.length - 1).toString.replace("\t", "     ")
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
