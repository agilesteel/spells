package spells

trait CustomRendering {
  implicit def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String
}

object CustomRendering extends SpellsConfig {
  object Defaults {
    object Any extends (Any => CustomRendering) {
      final def apply(any: Any): CustomRendering = new CustomRendering {
        def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
          String valueOf any
      }
    }

    final val AvailableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = new CustomRendering.AvailableWidthInCharacters(SpellsConfig.terminal.WidthInCharacters)
  }

  implicit class AvailableWidthInCharacters(val value: Int) {
    override final def toString: String = value.toString
  }

  implicit def availableWidthInCharactersBackToInt(availableWidthInCharacters: AvailableWidthInCharacters): Int =
    availableWidthInCharacters.value
}
