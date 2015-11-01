package object spells extends LocationAwareConfig {
  private[spells] object coverage {
    object `should-be-happy` extends LocationAwareProperty[Boolean]
  }

  object terminal {
    object `width-in-characters` extends LocationAwareProperty[Int]
  }
}
