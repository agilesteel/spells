package spells

trait CustomRendering {
  implicit def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String
}

object CustomRendering {
  object Defaults {
    object Any extends (Any => CustomRendering) {
      final def apply(any: Any): CustomRendering = new CustomRendering {
        def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
          String valueOf any
      }
    }

    final val AvailableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = new CustomRendering.AvailableWidthInCharacters(spells.terminal.`width-in-characters`)
  }

  implicit class AvailableWidthInCharacters(val value: Int) extends AnyVal {
    override final def toString: String = value.toString
  }

  implicit def availableWidthInCharactersBackToInt(availableWidthInCharacters: AvailableWidthInCharacters): Int =
    availableWidthInCharacters.value
}
