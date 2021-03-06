package spells

/** Contains custom rendering for `StackTraceElement`. */
trait StackTraceElementModule {
  this: CustomRenderingModule with StringOpsModule with SpellsConfigModule =>

  final implicit class StackTraceElementOpsFromSpells(value: StackTraceElement)
      extends CustomRendering {
    final override def rendered(
        implicit
        availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String = {
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

      if (SpellsConfig.`custom-rendering`.display.ShortStackTraceElements.value)
        fileNameOrUnknownSourceWithLineNumberOrNativeMethod
      else
        value.getClassName + "." + value.getMethodName + fileNameOrUnknownSourceWithLineNumberOrNativeMethod
    }
  }
}
