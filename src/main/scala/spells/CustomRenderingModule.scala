package spells

/** This module provides the `CustomRendering` trait, which is used throughout the whole library, especially in the `XrayModule`.
  */
trait CustomRenderingModule {
  this: StringOpsModule with SpellsConfigModule =>

  trait CustomRendering {
    /** Renders anything. Used as view bound in many places, like `xray`.
      * @param availableWidthInCharacters might be used by implementations
      * @return a `String` rendering.
      */
    def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String
  }

  object CustomRendering {
    object Defaults {
      object Any extends (Any => CustomRenderingModule#CustomRendering) {
        final def apply(any: Any): CustomRenderingModule#CustomRendering = new CustomRendering {
          override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String =
            String valueOf any
        }
      }
    }
  }
}
