package spells

trait XrayModule {
  this: AnsiModule with AnyOpsModule with CalendarOpsModule with CustomRenderingModule with DateOpsModule with DurationOpsModule with HumanRenderingModule with StringOpsModule with StylePrintModule with TraversableOpsModule with SpellsConfigModule =>

  import java.util.Calendar
  import scala.concurrent.duration._
  import scala.reflect.runtime.universe._

  final def xrayed[T](expression: => T, description: XrayModule#Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayModule#XrayReport[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    new XrayReport(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style, rendering)
  }

  def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit class XrayFromSpells[T](expression: => T)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, monitor: XrayModule#XrayReport[T] => Unit = (report: XrayModule#XrayReport[T]) => Console.println(report.rendered)) {
    def xray(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(typeTag, style, rendering)

      monitor(report)

      report.value
    }

    def xrayIf(conditionFunction: XrayModule#XrayReport[T] => Boolean)(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(typeTag, style, rendering)

      if (conditionFunction(report))
        monitor(report)

      report.value
    }
  }

  implicit class Description(val value: String) {
    override final def toString: String = value
  }

  object Xray {
    object Defaults {
      final val Description: XrayModule#Description = new Description("X-Ray")
    }
  }

  final class XrayReport[+T: TypeTag](
      final val value: T,
      final val duration: Duration,
      final val stackTraceElement: StackTraceElement,
      final val timestamp: Calendar,
      final val description: String,
      final val thread: Thread,
      final val style: AnsiModule#AnsiStyle = Reset,
      rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      def lines(availableWidthInCharacters: Int): Seq[(String, String)] = {
        val contentLines = {
          val metaContent = Vector(
            { if (SpellsConfig.xray.report.display.DateTime) Some("DateTime" -> timestamp.rendered) else None },
            { if (SpellsConfig.xray.report.display.Duration) Some("Duration" -> duration.rendered) else None },
            { if (SpellsConfig.xray.report.display.Location) Some("Location" -> stackTraceElement) else None },
            { if (SpellsConfig.xray.report.display.Thread) Some("Thread" -> thread) else None }
          )

          val classOrTypeOrBoth = {
            val `class` = value.decodedClassName
            val `type` = typeTag.tpe.toString.withDecodedScalaSymbols
            val classTyple = { if (SpellsConfig.xray.report.display.Class) Some("Class" -> `class`) else None }
            val typeTuple = { if (SpellsConfig.xray.report.display.Type) Some("Type" -> `type`) else None }

            if (`class` == `type`) Vector(typeTuple) else Vector(classTyple, typeTuple)
          }

          (metaContent ++ classOrTypeOrBoth).flatten
        }

        contentLines :+ "Value" -> (Option(value).fold("null")(rendering(_).rendered(availableWidthInCharacters))) map {
          case (key, value) => key.toString -> value.toString
        }
      }

      val table =
        XrayReport.customRenderedTableForXray(
          lines,
          styles = Map[String, AnsiModule#AnsiStyle](
            "DateTime" -> SpellsConfig.xray.report.styles.DateTime,
            "Duration" -> SpellsConfig.xray.report.styles.Duration,
            "Location" -> SpellsConfig.xray.report.styles.Location,
            "Thread" -> SpellsConfig.xray.report.styles.Thread,
            "Class" -> SpellsConfig.xray.report.styles.Class,
            "Type" -> SpellsConfig.xray.report.styles.Type,
            "Value" -> SpellsConfig.xray.report.styles.Value
          ) withDefaultValue Reset,
          availableWidthInCharacters
        )

      val numberOfCharsInTheLongestLine =
        table.map(Ansi.removedStyles).flatMap(_ split "\n").maxBy(_.size).size

      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min availableWidthInCharacters)

      val centeredHeader = {
        val headerStyleFromConfig: AnsiModule#AnsiStyle = SpellsConfig.xray.report.styles.Description
        val header = if (Ansi.removedStyles(description).isEmpty) styled("X-Ray")(headerStyleFromConfig) else styled(description)(headerStyleFromConfig)
        val emptySpace = hyphens.size - Ansi.removedStyles(header).size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + header
      }

      val metaContentPartOfTable = table.dropRight(1)
      val valuePartOfTable = if (metaContentPartOfTable.isEmpty) Vector(table.last) else Vector(hyphens, table.last)

      val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ metaContentPartOfTable ++ valuePartOfTable :+ hyphens

      styled(resultingLines mkString "\n")(style)
    }
  }

  object XrayReport {
    private[spells] final def customRenderedTableForXray(in: Int => Seq[(String, String)], styles: Map[String, AnsiModule#AnsiStyle] = Map.empty withDefaultValue Reset, availableWidthInCharacters: Int): Seq[String] = {
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
                      val theMatch = StylePrint.StyleOrReset.r findFirstIn takenSoFar.reverse
                      theMatch foreach (result = _)
                      theMatch.isEmpty
                    }

                    result.toAnsiStyle
                  }

                  val thisSubLine =
                    if (previousSublineStyle.value == Reset.value)
                      styled(subLine)(styles(key))
                    else
                      styled(subLine)(previousSublineStyle)

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
}
