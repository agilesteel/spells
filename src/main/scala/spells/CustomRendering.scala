package spells

trait CustomRendering {
  implicit def rendered(implicit availableWidthInCharacters: Int = spells.terminal.`width-in-characters`): String
}

object CustomRendering {
  object Defaults {
    object Any extends (Any => CustomRendering) {
      final def apply(any: Any): CustomRendering = new CustomRendering {
        def rendered(implicit availableWidthInCharacters: Int = spells.terminal.`width-in-characters`): String = String valueOf any
      }
    }
  }
}
