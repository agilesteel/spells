package spells

trait SpellsConfig extends LocationAwareConfig {
  object SpellsConfig {
    object terminal {
      object WidthInCharacters extends LocationAwareProperty[Int]
    }

    object xray {
      object report {
        object display {
          object DateTime extends LocationAwareProperty[Boolean]
          object Duration extends LocationAwareProperty[Boolean]
          object Location extends LocationAwareProperty[Boolean]
          object Thread extends LocationAwareProperty[Boolean]
          object Class extends LocationAwareProperty[Boolean]
          object Type extends LocationAwareProperty[Boolean]
        }

        object styles {
          object Description extends LocationAwareProperty[Ansi.Style]
          object DateTime extends LocationAwareProperty[Ansi.Style]
          object Duration extends LocationAwareProperty[Ansi.Style]
          object Location extends LocationAwareProperty[Ansi.Style]
          object Thread extends LocationAwareProperty[Ansi.Style]
          object Class extends LocationAwareProperty[Ansi.Style]
          object Type extends LocationAwareProperty[Ansi.Style]
          object Value extends LocationAwareProperty[Ansi.Style]
        }
      }
    }
  }
}
