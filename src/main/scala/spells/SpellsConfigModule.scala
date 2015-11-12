package spells

trait SpellsConfigModule extends LocationAwareConfigModule {
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
          object Description extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object DateTime extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Duration extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Location extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Thread extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Class extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Type extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Value extends LocationAwareProperty[AnsiModule#AnsiStyle]
        }
      }
    }
  }
}
