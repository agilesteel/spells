package spells

trait TraversableOps {
  this: Ansi with AnyOps with CalendarOps with DurationOps with HumanRendering with StringOps with StylePrint =>

  final implicit def TraversableOpsFromSpells[T](value: Traversable[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered: String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[Traversable[T]](value, _.isEmpty, _.size, value.toString, className, typeName) { in =>
        var result: Vector[(String, String)] = Vector.empty[(String, String)]
        var index = 0
        value foreach { element =>
          result :+= (index.toString -> element.rendered)
          index += 1
        }

        result
      }
    }
  }

  final implicit def ArrayOpsFromSpells[T](value: Array[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered: String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[Array[T]](value, _.isEmpty, _.size, s"Array()", className, typeName) { in =>
        var result: Vector[(String, String)] = Vector.empty[(String, String)]
        var index = 0
        value foreach { element =>
          result :+= (index.toString -> element.rendered)
          index += 1
        }

        result
      }
    }
  }

  final implicit def CollectionOpsFromSpells[T](value: java.util.Collection[T])(implicit manifest: Manifest[T], rendering: T => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered: String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[java.util.Collection[T]](value, _.isEmpty, _.size, s"$className()", className, typeName) { in =>
        var result: Vector[(String, String)] = Vector.empty[(String, String)]
        var index = 0
        val iterator = value.iterator
        while (iterator.hasNext) {
          val element = iterator.next
          result :+= (index.toString -> element.rendered)
          index += 1
        }

        result
      }
    }
  }

  final implicit def MapOpsFromSpells[Key, Value](value: java.util.Map[Key, Value])(implicit manifest: Manifest[java.util.Map.Entry[Key, Value]], rendering: java.util.Map.Entry[Key, Value] => CustomRendering = CustomRendering.Defaults.Any): CustomRendering = new CustomRendering {
    def rendered: String = {
      lazy val className = value.decodedClassName
      lazy val typeName = manifest.toString.withDecodedScalaSymbols

      render[java.util.Map[Key, Value]](value, _.isEmpty, _.size, s"$className()", className, typeName) { in =>
        var result: Vector[(String, String)] = Vector.empty[(String, String)]
        var index = 0
        val iterator = in.entrySet.iterator
        while (iterator.hasNext) {
          val element = iterator.next
          result :+= (index.toString -> element.rendered)
          index += 1
        }

        result
      }
    }
  }

  private def render[T](value: => T, isEmpty: T => Boolean, getSize: T => Int, emptyRendered: => String, className: => String, typeName: => String)(pairs: T => Seq[(String, String)]): String = {
    def nonEmptyRendered: String = {
      val size = getSize(value)
      val sizeString = if (size == 1) "1 element" else s"$size elements"

      val header = s"$className[$typeName] with $sizeString"
      header + ":\n\n" + renderedTable(pairs(value)).mkString("\n")
    }

    if (isEmpty(value)) emptyRendered else nonEmptyRendered
  }

  private[spells] def renderedTable(in: Seq[(String, String)], styles: Map[String, Ansi.Style] = Map.empty withDefaultValue Reset): Seq[String] = {
    if (in.isEmpty)
      Seq.empty
    else {
      val sizeOfTheBiggestKey = in map {
        case (key, _) => Ansi.removeStyles(key).size
      } max

      val separator = " | "

      val maxWidthInCharacters =
        spells.terminal.`width-in-characters` - separator.size - sizeOfTheBiggestKey

      in.foldLeft(Vector.empty[String]) {
        case (result, (key, value)) =>
          val keyWithPadding = key.padTo(sizeOfTheBiggestKey, ' ')
          val line = {
            val actualValue = value wrappedOnSpaces maxWidthInCharacters

            if (!(actualValue contains "\n"))
              keyWithPadding + separator + styled(actualValue)(styles(key))
            else {
              val subLines = actualValue.split("\n").toList
              val renderedHead = keyWithPadding + separator + styled(subLines.head)(styles(key)) + "\n"

              var previousSubLine = styled(subLines.head)(styles(key))

              val renderedTail = subLines.tail.map { subLine =>
                val previousSublineStyle = {
                  var takenSoFar = ""
                  var result = ""

                  previousSubLine.reverse.takeWhile { char =>
                    takenSoFar += char
                    val theMatch = StylePrint.styleOnly.r findFirstIn takenSoFar.reverse
                    theMatch foreach (result = _)
                    theMatch.isEmpty
                  }

                  result.toAnsiStyle
                }

                val thisSubLine = styled(subLine)(previousSublineStyle)
                val result = (" " * sizeOfTheBiggestKey) + separator + thisSubLine

                if (thisSubLine.nonEmpty)
                  previousSubLine = thisSubLine

                result
              }.mkString("\n")

              renderedHead + renderedTail
            }
          }

          result :+ line
      }
    }
  }
}
