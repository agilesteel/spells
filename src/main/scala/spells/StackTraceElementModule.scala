package spells

/** Contains custom rendering for `StackTraceElement`. */
trait StackTraceElementModule {
  this: CustomRenderingModule with SpellsConfigModule =>

  implicit final class StackTraceElementOpsFromSpells(value: StackTraceElement) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      val fileNameOrUnknownSource =
        if (value.getFileName != null)
          "(" + value.getFileName + ")"
        else "(Unknown Source)"

      val fileNameOrUnknownSourceWithLineNumber =
        if (value.getFileName != null && value.getLineNumber >= 0)
          "(" + value.getFileName + ":" + value.getLineNumber + ")"
        else fileNameOrUnknownSource

      val fileNameOrUnknownSourceWithLineNumberOrNativeMethod =
        if (value.isNativeMethod)
          "(Native Method)"
        else fileNameOrUnknownSourceWithLineNumber

      if (SpellsConfig.`custom-rendering`.display.ShortStackTraceElements)
        fileNameOrUnknownSourceWithLineNumberOrNativeMethod
      else value.getClassName + "." + value.getMethodName + fileNameOrUnknownSourceWithLineNumberOrNativeMethod
    }
  }
}
