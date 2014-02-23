package object spells extends LocationAwareConfig with Spells {
  private[spells] object feature {
    object `copy-file-path-to-clipboard-when-debug-printing` extends LocationAwareProperty
  }
}
