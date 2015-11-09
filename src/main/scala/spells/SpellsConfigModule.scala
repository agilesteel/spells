package spells

trait SpellsConfigModule extends LocationAwareConfig {
  this: AnsiModule =>

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
          object Description extends LocationAwareProperty[AnsiModule.Style]
          object DateTime extends LocationAwareProperty[AnsiModule.Style]
          object Duration extends LocationAwareProperty[AnsiModule.Style]
          object Location extends LocationAwareProperty[AnsiModule.Style]
          object Thread extends LocationAwareProperty[AnsiModule.Style]
          object Class extends LocationAwareProperty[AnsiModule.Style]
          object Type extends LocationAwareProperty[AnsiModule.Style]
          object Value extends LocationAwareProperty[AnsiModule.Style]
        }
      }
    }
  }
}
