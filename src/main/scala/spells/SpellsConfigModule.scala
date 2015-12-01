package spells

trait SpellsConfigModule extends LocationAwareConfigModule with LocationAwarePropertyModule {
  this: StylePrintModule with AnsiModule =>

  object SpellsConfig {
    object `custom-rendering` {
      object display {
        object ShortStackTraceElements extends LocationAwareProperty[Boolean]
      }
    }

    object terminal {
      object WidthInCharacters extends LocationAwareProperty[Int] {
        override def isValid(in: Int): Boolean = in >= 0
        override def customValidationErrorMessage(in: Int): String = s"${location.yellow} must be ${">= 0".yellow}"
      }

      object display {
        object Styles extends LocationAwareProperty[Boolean]
      }
    }

    object xray {
      object report {
        object IgnoredContentKeys extends LocationAwareProperty[List[String]]

        object styles {
          object Description extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object DateTime extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Duration extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Location extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object HashCode extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Thread extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Class extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Type extends LocationAwareProperty[AnsiModule#AnsiStyle]
          object Value extends LocationAwareProperty[AnsiModule#AnsiStyle]
        }
      }
    }
  }
}
