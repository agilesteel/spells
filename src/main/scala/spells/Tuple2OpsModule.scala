package spells

/** Provides utility methods for `Tuple2[Key, Value]`. */
trait Tuple2OpsModule {
  this: CustomRenderingModule with SpellsConfigModule with StringOpsModule =>

  implicit final def Tuple2OpsFromSpells[Key, Value](tuple: (Key, Value))(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val (key, value) = tuple

    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
      s"${key.rendered} -> ${value.rendered}"
  }

  implicit final def MapEntryOpsFromSpells[Key, Value](entry: java.util.Map.Entry[Key, Value])(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val key = entry.getKey
    val value = entry.getValue

    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
      s"${key.rendered} -> ${value.rendered}"
  }
}
