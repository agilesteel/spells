package spells

trait TraversableOps {
  this: Ansi with AnyOps with CalendarOps with DurationOps with HumanRendering with StringOps with StylePrint =>

  final implicit def TraversableOpsFromSpells[T](value: Traversable[T])(implicit manifest: Manifest[T], evidence: T => CustomRendering = CustomRendering.Default.apply(_: T)): CustomRendering = new CustomRendering {
    def rendered: String = {
      def nonEmptyRendered: String = {
        val `class` = value.decodedClassName
        val `type` = manifest.toString.withDecodedScalaSymbols

        val size = value.size
        val sizeString = if (size == 1) "1 element" else s"$size elements"

        val header = s"${`class`}[${`type`}] with $sizeString"
        val x = {
          var result: List[(String, String)] = List.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result ::= (index.toString -> element.rendered)
            index += 1
          }

          result.reverse
        }

        header + ":\n\n" + renderedTable(x).mkString("\n")
      }

      if (value.isEmpty) value.toString else nonEmptyRendered
    }
  }

  private[spells] def renderedTable(in: Seq[(String, String)], styles: Map[String, Ansi#AnsiStyle] = Map.empty withDefaultValue Reset): Seq[String] = {
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

  implicit class ArrayOpsFromSpells[T](value: Array[T]) extends CustomRendering {
    def rendered: String = ""
  }
}
