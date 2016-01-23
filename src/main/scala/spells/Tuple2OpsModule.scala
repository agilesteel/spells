package spells

/** Provides utility methods for `Tuple2[Key, Value]`. */
trait Tuple2OpsModule {
  this: AnsiModule with CustomRenderingModule with SpellsConfigModule with StringOpsModule with StylePrintModule =>

  implicit final def Tuple2OpsFromSpells[Key, Value](tuple: (Key, Value))(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val (key, value) = tuple

    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String = {
      render(key.rendered, value.rendered, availableWidthInCharacters)
    }
  }

  implicit final def MapEntryOpsFromSpells[Key, Value](entry: java.util.Map.Entry[Key, Value])(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val key = entry.getKey
    val value = entry.getValue

    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
      render(key.rendered, value.rendered, availableWidthInCharacters)
  }

  private def render(renderedKey: String, renderedValue: String, availableWidthInCharacters: Int): String = {
    val renderedAndWrappedKey = renderedKey.wrappedOnSpaces(availableWidthInCharacters)
    val renderedAndWrappedValue = renderedValue.wrappedOnSpaces(availableWidthInCharacters)

    val separator = " -> "
    val leftPadding = "  "

    lazy val simpleRendering = s"$renderedAndWrappedKey$separator$renderedAndWrappedValue"

    lazy val complexRendering =
      (renderedAndWrappedKey + separator).wrappedOnSpaces(availableWidthInCharacters) + "\n" +
        (renderedValue.wrappedOnSpaces(availableWidthInCharacters - leftPadding.size)).split("\n").toList.map(leftPadding + _).mkString("\n")

    if (!simpleRendering.contains("\n") && AnsiStyle.removed(simpleRendering).size <= availableWidthInCharacters)
      simpleRendering
    else if (renderedAndWrappedValue.contains("\n"))
      complexRendering
    else if (AnsiStyle.removed(renderedAndWrappedValue).size <= (availableWidthInCharacters - AnsiStyle.removed(renderedAndWrappedKey.split("\n").last).size - separator.size))
      simpleRendering
    else
      complexRendering
  }
}
