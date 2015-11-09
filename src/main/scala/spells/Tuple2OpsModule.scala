package spells

trait Tuple2OpsModule {
  final implicit def Tuple2OpsFromSpells[Key, Value](tuple: (Key, Value))(implicit keyRendering: Key => CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    val (key, value) = tuple

    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters) = s"${key.rendered} -> ${value.rendered}"
  }

  final implicit def MapEntryOpsFromSpells[Key, Value](entry: java.util.Map.Entry[Key, Value])(implicit keyRendering: Key => CustomRendering = CustomRendering.Defaults.Any, valueRendering: Value => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    val key = entry.getKey
    val value = entry.getValue

    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters) = s"${key.rendered} -> ${value.rendered}"
  }
}
