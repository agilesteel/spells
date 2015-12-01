package spells

trait XrayModule {
  this: AnsiModule with AnyOpsModule with CalendarOpsModule with CustomRenderingModule with DateOpsModule with DurationOpsModule with HumanRenderingModule with StringOpsModule with StylePrintModule with TraversableOpsModule with SpellsConfigModule with StackTraceElementModule =>

  import java.util.Calendar
  import scala.concurrent.duration._
  import scala.reflect.runtime.universe._
  import scala.collection.immutable
  import scala.util.matching.Regex

  final def xrayed[T](expression: => T, description: XrayModule#Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayModule#XrayReport[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    new XrayReport(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style, rendering, Some(typeTag))
  }

  final def xrayedWeak[T](expression: => T, description: XrayModule#Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayModule#XrayReport[T] = {
    val stackTraceElement = currentLineStackTraceElement(increaseStackTraceDepthBy - 1)

    val now = Calendar.getInstance

    val start = System.nanoTime
    val value = expression
    val stop = System.nanoTime - start

    new XrayReport(value, stop.nanos, stackTraceElement, now, description.toString, Thread.currentThread, style, rendering, typeTag = None)
  }

  final def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: Int = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy + 6

  implicit final class XrayFromSpells[T](expression: => T)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, monitor: XrayModule#XrayReport[T] => Unit = (report: XrayModule#XrayReport[T]) => Console.println(report.rendered)) {
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

  implicit final class XrayWeakFromSpells[T](expression: => T)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, monitor: XrayModule#XrayReport[T] => Unit = (report: XrayModule#XrayReport[T]) => Console.println(report.rendered)) {
    def xrayWeak(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayedWeak(expression, description, increaseStackTraceDepthBy = +1)(style, rendering)

      monitor(report)

      report.value
    }

    def xrayWeakIf(conditionFunction: XrayModule#XrayReport[T] => Boolean)(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayedWeak(expression, description, increaseStackTraceDepthBy = +1)(style, rendering)

      if (conditionFunction(report))
        monitor(report)

      report.value
    }
  }

  implicit final class Description(val value: String) {
    override final def toString: String = value
  }

  object Xray {
    object Defaults {
      final val Description: XrayModule#Description = new Description("X-Ray")
    }
  }

  final class XrayReport[+T](
      final val value: T,
      final val duration: Duration,
      final val stackTraceElement: StackTraceElement,
      final val timestamp: Calendar,
      final val description: String,
      final val thread: Thread,
      final val style: AnsiModule#AnsiStyle = AnsiStyle.Reset,
      rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any,
      typeTag: Option[TypeTag[T]],
      final val additionalContent: immutable.Seq[(String, String)] = immutable.Seq.empty
  ) extends CustomRendering {
    private lazy val safeAdditionalContent: immutable.Seq[(String, String)] = Option(additionalContent).getOrElse(immutable.Seq.empty)

    final def withAdditionalContent(content: immutable.Seq[(String, String)]): XrayModule#XrayReport[T] =
      new XrayReport(value, duration, stackTraceElement, timestamp, description, thread, style, rendering, typeTag, safeAdditionalContent ++ Option(content).getOrElse(immutable.Seq.empty))

    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String = {
      def lines(availableWidthInCharacters: Int): Seq[(String, String)] = {
        def ifNotIgnored(key: String, value: String): Option[(String, String)] =
          if (SpellsConfig.xray.report.IgnoredContentKeys.contains(String.valueOf(key)))
            None
          else
            Some(String.valueOf(key) -> String.valueOf(value))

        val contentLines = {
          val metaContent = Vector(
            ifNotIgnored("DateTime", timestamp.rendered),
            ifNotIgnored("Duration", duration.rendered)
          )

          val valueRelatedContent = Vector(
            ifNotIgnored("Location", stackTraceElement.rendered),
            { if (value == null) None else ifNotIgnored("HashCode", value.hashCode.toString) },
            ifNotIgnored("Thread", thread.toString)
          )

          val classOrTypeOrBoth = {
            val decodedClassName = value.decodedClassName
            val classTuple = ifNotIgnored("Class", decodedClassName)

            typeTag.fold(Vector(classTuple)) { tag =>
              val decodedTypeName = tag.tpe.toString.withDecodedScalaSymbols
              val typeTuple = ifNotIgnored("Type", decodedTypeName)
              val shouldNotIgnoreClass = !SpellsConfig.xray.report.IgnoredContentKeys.contains("Class")
              val shouldIgnoreType = SpellsConfig.xray.report.IgnoredContentKeys.contains("Type")

              if (shouldIgnoreType && shouldNotIgnoreClass) Vector(classTuple)
              else if (decodedClassName == decodedTypeName) Vector(typeTuple)
              else Vector(classTuple, typeTuple)
            }
          }

          val liftedAdditionalContentIfNotIgnored: immutable.Seq[Option[(String, String)]] =
            safeAdditionalContent collect {
              case (key, value) => ifNotIgnored(key, value)
            }

          (metaContent ++ liftedAdditionalContentIfNotIgnored ++ valueRelatedContent ++ classOrTypeOrBoth).flatten
        }

        contentLines :+ "Value" -> (Option(value).fold("null") {
          case in: CustomRendering => in.rendered(availableWidthInCharacters)
          case in => rendering(in).rendered(availableWidthInCharacters)
        }) map {
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
          "HashCode" -> SpellsConfig.xray.report.styles.HashCode,
          "Thread" -> SpellsConfig.xray.report.styles.Thread,
          "Class" -> SpellsConfig.xray.report.styles.Class,
          "Type" -> SpellsConfig.xray.report.styles.Type,
          "Value" -> SpellsConfig.xray.report.styles.Value
        ) withDefaultValue AnsiStyle.Reset,
          availableWidthInCharacters
        )

      val numberOfCharsInTheLongestLine =
        table.map(AnsiStyle.removed).flatMap(_ split "\n").maxBy(_.size).size

      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min availableWidthInCharacters)

      val centeredHeader = {
        val headerStyleFromConfig: AnsiModule#AnsiStyle = SpellsConfig.xray.report.styles.Description
        val header = if (AnsiStyle.removed(description).isEmpty) styled("X-Ray")(headerStyleFromConfig) else styled(description)(headerStyleFromConfig)
        val emptySpace = hyphens.size - AnsiStyle.removed(header).size
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
    private[spells] final def customRenderedTableForXray(in: Int => Seq[(String, String)], styles: Map[String, AnsiModule#AnsiStyle] = Map.empty withDefaultValue AnsiStyle.Reset, availableWidthInCharacters: Int): Seq[String] = {
      if (in(0).isEmpty) Seq.empty
      else {
        val sizeOfTheBiggestKey = in(0) map {
          case (key, _) => AnsiStyle.removed(key).size
        } max

        val spaces = (" " * sizeOfTheBiggestKey)
        val separator = " | "

        val maxWidthInCharacters =
          availableWidthInCharacters - separator.size - sizeOfTheBiggestKey

        in(maxWidthInCharacters).foldLeft(Vector.empty[String]) {
          case (result, (key, value)) =>
            val keyPaddedWithSpaces = key.padTo(sizeOfTheBiggestKey, ' ')
            val line = {
              val actualValue = value wrappedOnSpaces maxWidthInCharacters

              if (!(actualValue contains "\n"))
                keyPaddedWithSpaces + separator + styled(actualValue)(styles(key))
              else {
                val sublines = actualValue.split("\n").toList

                var previousSublineStyle = "".toAnsiStyle

                sublines.map { subline =>
                  val styledSubline =
                    if (previousSublineStyle.value.nonEmpty)
                      styled(subline)(previousSublineStyle)
                    else
                      styled(subline)(styles(key))

                  if (previousSublineStyle.value.isEmpty)
                    previousSublineStyle = fetchLastStyleBasedOnRegex(styledSubline, StylePrint.StyleOnly.r)

                  spaces + separator + styledSubline
                }.mkString("\n").replaceFirst(spaces, keyPaddedWithSpaces)
              }
            }

            result :+ line
        }
      }
    }

    private[spells] final def fetchLastStyleBasedOnRegex(line: String, regex: Regex): AnsiModule#AnsiStyle = {
      var style = ""
      var takenSoFar = ""

      line.reverse takeWhile { char =>
        takenSoFar += char
        val theMatch: Option[String] = regex findFirstIn takenSoFar.reverse
        theMatch foreach (style = _)
        theMatch.isEmpty
      }

      style.toAnsiStyle
    }
  }
}
