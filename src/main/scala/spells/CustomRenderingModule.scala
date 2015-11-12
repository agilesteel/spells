package spells

trait CustomRenderingModule {
  this: SpellsConfigModule =>

  trait CustomRendering {
    implicit def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String
  }

  object CustomRendering {
    object Defaults {
      object Any extends (Any => CustomRenderingModule#CustomRendering) {
        final def apply(any: Any): CustomRenderingModule#CustomRendering = new CustomRendering {
          def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
            String valueOf any
        }
      }

      final val AvailableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = new AvailableWidthInCharacters(SpellsConfig.terminal.WidthInCharacters)
    }
  }

  implicit class AvailableWidthInCharacters(val value: Int) {
    override final def toString: String = value.toString
  }

  implicit def availableWidthInCharactersBackToInt(availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters): Int =
    availableWidthInCharacters.value
}
