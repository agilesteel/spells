package object spells extends Spells with LocationAwareConfig {
  private[spells] object feature {
    object `copy-file-path-to-clipboard-when-debug-printing` extends LocationAwareProperty[Boolean]
  }
}
