package object spells extends LocationAwareConfig {
  // private[spells] object coverage {
  //   object `should-be-happy` extends LocationAwareProperty[Boolean]
  // }

  object terminal {
    object WidthInCharacters extends LocationAwareProperty[Int]
  }

  object XrayReport {
    object DisplayDateTime extends LocationAwareProperty[Boolean]
    object DisplayDuration extends LocationAwareProperty[Boolean]
    object DisplayLocation extends LocationAwareProperty[Boolean]
    object DisplayThread extends LocationAwareProperty[Boolean]
    object DisplayClass extends LocationAwareProperty[Boolean]
    object DisplayType extends LocationAwareProperty[Boolean]

    object DescriptionStyle extends LocationAwareProperty[Ansi.Style]
    object DateTimeStyle extends LocationAwareProperty[Ansi.Style]
    object DurationStyle extends LocationAwareProperty[Ansi.Style]
    object LocationStyle extends LocationAwareProperty[Ansi.Style]
    object ThreadStyle extends LocationAwareProperty[Ansi.Style]
    object ClassStyle extends LocationAwareProperty[Ansi.Style]
    object TypeStyle extends LocationAwareProperty[Ansi.Style]
    object ValueStyle extends LocationAwareProperty[Ansi.Style]
  }
}
