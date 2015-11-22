package spells

trait CustomRenderingModule {
  this: SpellsConfigModule =>

  trait CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String
  }

  object CustomRendering {
    object Defaults {
      object Any extends (Any => CustomRenderingModule#CustomRendering) {
        final def apply(any: Any): CustomRenderingModule#CustomRendering = new CustomRendering {
          override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
            String valueOf any
        }
      }

      final val AvailableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = new AvailableWidthInCharacters(SpellsConfig.terminal.WidthInCharacters)
    }
  }

  implicit final class AvailableWidthInCharacters(val value: Int) {
    override final def toString: String = value.toString
  }

  implicit final def availableWidthInCharactersBackToInt(availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters): Int =
    availableWidthInCharacters.value
}
