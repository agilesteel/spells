package spells

/**
 * This module provides the `CustomRendering` trait, which is used throughout the whole library, especially in the `XrayModule`.
 */
trait CustomRenderingModule {
  this: SpellsConfigModule =>

  trait CustomRendering {
    /**
     * Renders anything. Used as view bound in many places, like `xray`.
     * @param availableWidthInCharacters might be used by implementations
     * @return a `String` rendering.
     */
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

  /**
   * A wrapper for `Int`s, provided so that it can be used as an `implicit` parameter, which `Int`s are not ideal for.
   * @param value the `Int` to be wrapped.
   */
  implicit final class AvailableWidthInCharacters(val value: Int) {
    override final def toString: String = value.toString
  }

  implicit final def availableWidthInCharactersBackToInt(availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters): Int =
    availableWidthInCharacters.value
}
