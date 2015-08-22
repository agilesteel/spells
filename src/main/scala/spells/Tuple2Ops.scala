package spells

trait Tuple2Ops {
  final implicit def Tuple2OpsFromSpells[Key, Value](tuple: (Key, Value))(implicit evidence1: Key => CustomRendering = CustomRendering.Defaults.Any, evidence2: Value => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    val (key, value) = tuple

    def rendered = s"${key.rendered} -> ${value.rendered}"
  }

  final implicit def MapEntryOpsFromSpells[Key, Value](entry: java.util.Map.Entry[Key, Value])(implicit evidence1: Key => CustomRendering = CustomRendering.Defaults.Any, evidence2: Value => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    val key = entry.getKey
    val value = entry.getValue

    def rendered = s"${key.rendered} -> ${value.rendered}"
  }
}
