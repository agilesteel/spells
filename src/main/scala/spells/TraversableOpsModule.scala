package spells

trait TraversableOpsModule {
  this: AnsiModule with AnyOpsModule with CalendarOpsModule with DurationOpsModule with HumanRenderingModule with StringOpsModule with StylePrintModule =>

  final implicit def TraversableOpsFromSpells[T](value: Traversable[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[Traversable[T]](value, _.isEmpty, _.size, value.toString, className, typeName, availableWidthInCharacters) { in =>
        { availableWidthInCharacters =>
          var result: Vector[(String, String)] = Vector.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result :+= (index.toString -> element.rendered(availableWidthInCharacters))
            index += 1
          }

          result
        }
      }
    }
  }

  final implicit def ArrayOpsFromSpells[T](value: Array[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[Array[T]](value, _.isEmpty, _.size, s"Array()", "Array", typeName, availableWidthInCharacters) { in =>
        { availableWidthInCharacters =>
          var result: Vector[(String, String)] = Vector.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result :+= (index.toString -> element.rendered(availableWidthInCharacters))
            index += 1
          }

          result
        }
      }
    }
  }

  final implicit def CollectionOpsFromSpells[T](value: java.util.Collection[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[java.util.Collection[T]](value, _.isEmpty, _.size, s"$className()", className, typeName, availableWidthInCharacters) { in =>
        { availableWidthInCharacters =>
          var result: Vector[(String, String)] = Vector.empty[(String, String)]
          var index = 0
          val iterator = value.iterator
          while (iterator.hasNext) {
            val element = iterator.next
            result :+= (index.toString -> element.rendered(availableWidthInCharacters))
            index += 1
          }

          result
        }
      }
    }
  }

  final implicit def MapOpsFromSpells[Key, Value](value: java.util.Map[Key, Value])(implicit manifest: Manifest[java.util.Map.Entry[Key, Value]], rendering: java.util.Map.Entry[Key, Value] => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[java.util.Map[Key, Value]](value, _.isEmpty, _.size, s"$className()", className, typeName, availableWidthInCharacters) { in =>
        { availableWidthInCharacters =>
          var result: Vector[(String, String)] = Vector.empty[(String, String)]
          var index = 0
          val iterator = in.entrySet.iterator
          while (iterator.hasNext) {
            val element = iterator.next
            result :+= (index.toString -> element.rendered(availableWidthInCharacters))
            index += 1
          }

          result
        }
      }
    }
  }

  private final def render[T](value: => T, isEmpty: T => Boolean, getSize: T => Int, emptyRendered: => String, className: => String, typeName: => String, availableWidthInCharacters: Int)(pairs: T => Int => Seq[(String, String)]): String = {
    def nonEmptyRendered: String = {
      val size = getSize(value)
      val sizeString = if (size == 1) "1 element" else s"$size elements"

      val header = s"$className[$typeName] with $sizeString"
      header + ":\n\n" + renderedTable(pairs(value), availableWidthInCharacters).mkString("\n")
    }

    if (isEmpty(value)) emptyRendered else nonEmptyRendered
  }

  private[spells] final def renderedTable(in: Int => Seq[(String, String)], availableWidthInCharacters: Int): Seq[String] = {
    if (in(0).isEmpty) Seq.empty
    else {
      val sizeOfTheBiggestKey = in(0) map {
        case (key, _) => Ansi.removedStyles(key).size
      } max

      val separator = " | "

      val maxWidthInCharacters =
        availableWidthInCharacters - separator.size - sizeOfTheBiggestKey

      in(maxWidthInCharacters).foldLeft(Vector.empty[String]) {
        case (result, (key, value)) =>
          val keyWithPadding = key.padTo(sizeOfTheBiggestKey, ' ')
          val line = {
            val wrappedValue = value wrappedOnSpaces maxWidthInCharacters

            if (!(wrappedValue contains "\n"))
              keyWithPadding + separator + wrappedValue
            else {
              val subLines = wrappedValue.split("\n").toList
              val head = keyWithPadding + separator + subLines.head
              (head :: subLines.tail.map(subLine => (" " * keyWithPadding.size) + separator + subLine)).mkString("\n")
            }
          }

          result :+ line
      }
    }
  }
}
