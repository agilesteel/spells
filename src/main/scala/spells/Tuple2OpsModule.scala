package spells

trait Tuple2OpsModule {
  this: CustomRenderingModule =>

  final implicit def Tuple2OpsFromSpells[Key, Value](tuple: (Key, Value))(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val (key, value) = tuple

    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      s"${key.rendered} -> ${value.rendered}"
  }

  final implicit def MapEntryOpsFromSpells[Key, Value](entry: java.util.Map.Entry[Key, Value])(implicit keyRendering: Key => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): CustomRenderingModule#CustomRendering = new CustomRendering {
    val key = entry.getKey
    val value = entry.getValue

    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      s"${key.rendered} -> ${value.rendered}"
  }
}
