package object spells extends Spells with LocationAwareConfig {
  private[spells] object coverage {
    object `should-be-happy` extends LocationAwareProperty[Boolean]
  }

  private[spells] object terminal {
    object `width-in-characters` extends LocationAwareProperty[Int]
  }
}
