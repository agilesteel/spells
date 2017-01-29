package spells

/** Provides the `xrayed` as well as `xrayedWeak` methods respectively,
  * which analyse an expression and return an instance of the `XrayReport`.
  *
  * It also provides the `xray` as well as `xrayIf` method respectively as well as their "weak" counterparts,
  * which is a DSL for creating `XrayReport`s without interrupting the code flow.
  *
  * {{{
  * List(1,2,3).map(_ + 1)           // compiles and has same semantics
  * List(1,2,3).xray.map(_ + 1)      // compiles and has same semantics
  * List(1,2,3).xray.map(_ + 1).xray // compiles and has same semantics
  * }}}
  */
trait XrayModule {
  this: AnsiModule with AnyOpsModule with CalendarOpsModule with CustomRenderingModule with DateOpsModule with DurationOpsModule with HumanRenderingModule with MiscModule with StringOpsModule with StylePrintModule with TraversableOpsModule with SpellsConfigModule with StackTraceElementModule =>

  import java.util.Calendar
  import scala.concurrent.duration._
  import scala.reflect.runtime.universe._
  import scala.collection.immutable
  import scala.util.matching.Regex

  /** Creates an instance of `XrayReport`. Primarily useful for library authors.
    * @param expression the expression to be evaluated
    * @param description an optional description
    * @param increaseStackTraceDepthBy the depth can be used in certain cases when you want to write your own library and have issues with line numberes jumping around
    * @param typeTag the typeTag injected by the compiler
    * @param style outer style for the report
    * @param rendering custom rendering for `T`
    * @tparam T the type, your expression evaluates to
    * @return an instance of `XrayReport`, which can be rendered or written to a database etc etc
    */
  final def xrayed[T](expression: => T, description: XrayModule#Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any): XrayModule#XrayReport[T] = {
    val now = Calendar.getInstance

    val (value, duration) = measureExecutionTime(expression)

    val stackTraceElement = currentLineStackTraceElement(Xray.Defaults.StackTraceDepthOffset)

    new XrayReport(value, duration, stackTraceElement, now, description.toString, Thread.currentThread, style, rendering, Some(typeTag))
  }

  /** Creates an instance of `XrayReport`. Primarily useful for library authors.
    * @param expression the expression to be evaluated
    * @param description an optional description
    * @param increaseStackTraceDepthBy the depth can be used in certain cases when you want to write your own library and have issues with line numberes jumping around
    * @param style outer style for the report
    * @param rendering custom rendering for `T`
    * @tparam T the type, your expression evaluates to
    * @return an instance of `XrayReport`, which can be rendered or written to a database etc etc
    */
  final def xrayedWeak[T](expression: => T, description: XrayModule#Description = Xray.Defaults.Description, increaseStackTraceDepthBy: Int = 0)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset): XrayModule#XrayReport[T] = {
    val now = Calendar.getInstance

    val (value, duration) = measureExecutionTime(expression)

    val stackTraceElement = currentLineStackTraceElement(Xray.Defaults.StackTraceDepthOffset)

    new XrayReport(value, duration, stackTraceElement, now, description.toString, Thread.currentThread, style, CustomRendering.Defaults.Any, typeTag = None)
  }

  /** Creates an instance of `StackTraceElement` at current line.
    * @param increaseStackTraceDepthBy adjust if you build a library around it and the line stopps matching
    * @return an instance of `StackTraceElement` at current line.
    */
  final def currentLineStackTraceElement(implicit increaseStackTraceDepthBy: XrayModule#IncreaseStackTraceDepthBy = 0): StackTraceElement =
    Thread.currentThread.getStackTrace apply increaseStackTraceDepthBy.value + Xray.Defaults.StackTraceDepthOffset

  /** Implicit conversion from `T` to `XrayFromSpells`, which contains methods like `xray` and `xrayIf`.
    * @param expression the expression to be evaluated
    * @param typeTag the typeTag injected by the compiler
    * @param style outer style for the report
    * @param rendering custom rendering for `T`
    * @param monitor a monitor, which tracks the side effect (a `println` essentially)
    * @tparam T the type, your expression evaluates to
    */
  implicit final class XrayFromSpells[T](expression: => T)(implicit typeTag: TypeTag[T], style: AnsiModule#AnsiStyle = AnsiStyle.Reset, rendering: T => CustomRenderingModule#CustomRendering = CustomRendering.Defaults.Any, monitor: XrayModule#XrayReport[T] => Unit = (report: XrayModule#XrayReport[T]) => Console.println(report.rendered)) {
    /** A DSL for producing `XrayReport`s.
      *
      * Example:
      * {{{
      * Array(1, 2, 3).xray.map(_ + 1).xray
      * }}}
      * Use `xrayWeak` if `xray` does not compile.
      * @param description an optional description
      * @return the original value of the expression it is being called on
      */
    def xray(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(typeTag, style, rendering)

      monitor(report)

      report.value
    }

    /** A DSL for producing `XrayReport`s.
      * Example:
      * {{{
      * Array(1, 2, 3).xrayIf(_.value contains 2).map(_ + 1).xrayIf(_.duration > 3.seconds)
      * }}}
      * @param description an optional description
      * @return the original value of the expression it is being called on
      */
    def xrayIf(conditionFunction: XrayModule#XrayReport[T] => Boolean)(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayed(expression, description, increaseStackTraceDepthBy = +1)(typeTag, style, rendering)

      if (conditionFunction(report))
        monitor(report)

      report.value
    }
  }

  implicit final class XrayWeakFromSpells[T](expression: => T)(implicit style: AnsiModule#AnsiStyle = AnsiStyle.Reset, monitor: XrayModule#XrayReport[T] => Unit = (report: XrayModule#XrayReport[T]) => Console.println(report.rendered)) {
    /** A DSL for producing `XrayReport`s when `xray` does not compile, because of the `TypeTag`.
      *
      * Example:
      * {{{
      * def m[T](t: T): T = t.xrayWeak
      * }}}
      * @param description an optional description
      * @return the original value of the expression it is being called on
      */
    def xrayWeak(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayedWeak(expression, description, increaseStackTraceDepthBy = +1)(style)

      monitor(report)

      report.value
    }

    /** A DSL for producing `XrayReport`s when `xrayIf` does not compile, because of the `TypeTag`.
      *
      * Example:
      * {{{
      * def m[T](t: T): T = t.xrayIfWeak(_ => true)
      * }}}
      * @param description an optional description
      * @return the original value of the expression it is being called on
      */
    def xrayIfWeak(conditionFunction: XrayModule#XrayReport[T] => Boolean)(implicit description: XrayModule#Description = Xray.Defaults.Description): T = {
      val report = xrayedWeak(expression, description, increaseStackTraceDepthBy = +1)(style)

      if (conditionFunction(report))
        monitor(report)

      report.value
    }
  }

  /** A wrapper for `Int`s, provided so that it can be used as an `implicit` parameter, which `Int`s are not ideal for.
    * @param value the `Int` to be wrapped.
    */
  implicit final class IncreaseStackTraceDepthBy(val value: Int)

  /** A wrapper for `String`s, provided so that it can be used as an `implicit` parameter, which `String`s are not ideal for.
    * @param value the `String` to be wrapped.
    */
  implicit final class Description(val value: String) {
    override final def toString: String = value
  }

  object Xray {
    object Defaults {
      final val Description: XrayModule#Description = new Description("Xray")

      private[spells] final val StackTraceDepthOffset: Int = {
        // $COVERAGE-OFF$
        if (`isScalaVersionSmallerThan 2.12`) 3 else 4
        // $COVERAGE-ON$
      }

      private def `isScalaVersionSmallerThan 2.12`: Boolean =
        SemVer.major == 2 && SemVer.minor < 12

      private object SemVer {
        private val version = SpellsBuildInfo.scalaVersion.split("\\.")

        val major = version.head.toInt
        val minor = version.tail.head.toInt
        val patch = version.tail.tail.head.toInt
      }
    }
  }

  /** Instances of this class are created by methods like `xray` or `xrayIf`.
    * They are used to describe evaluated expressions and can be rendered as a table.
    *
    * @param value
    * @param duration
    * @param stackTraceElement
    * @param timestamp
    * @param description
    * @param thread
    * @param style
    * @param rendering
    * @param typeTag
    * @param additionalContent
    * @tparam T
    */
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

    override final def rendered(implicit availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters = SpellsConfig.terminal.WidthInCharacters.value): String = {
      def lines(availableWidthInCharacters: Int): Seq[(String, String)] = {
        def ifNotIgnored(key: String, value: String): Option[(String, String)] =
          if (SpellsConfig.xray.report.IgnoredContentKeys.value.contains(String.valueOf(key)))
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
              val shouldNotIgnoreClass = !SpellsConfig.xray.report.IgnoredContentKeys.value.contains("Class")
              val shouldIgnoreType = SpellsConfig.xray.report.IgnoredContentKeys.value.contains("Type")

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
          "DateTime" -> SpellsConfig.xray.report.styles.DateTime.value,
          "Duration" -> SpellsConfig.xray.report.styles.Duration.value,
          "Location" -> SpellsConfig.xray.report.styles.Location.value,
          "HashCode" -> SpellsConfig.xray.report.styles.HashCode.value,
          "Thread" -> SpellsConfig.xray.report.styles.Thread.value,
          "Class" -> SpellsConfig.xray.report.styles.Class.value,
          "Type" -> SpellsConfig.xray.report.styles.Type.value,
          "Value" -> SpellsConfig.xray.report.styles.Value.value
        ) withDefaultValue AnsiStyle.Reset,
          availableWidthInCharacters
        )

      val headerStyleFromConfig: AnsiModule#AnsiStyle = SpellsConfig.xray.report.styles.Description.value
      val header =
        if (AnsiStyle.removed(description).isEmpty)
          styled(Xray.Defaults.Description)(headerStyleFromConfig)
        else styled(description)(headerStyleFromConfig)
      val headerWithoutStyles = AnsiStyle.removed(header)

      val numberOfCharsInTheLongestLine = {
        val longestLineInHeaderWithoutStyles = headerWithoutStyles.split("\n").maxBy(_.size).size

        table.map(AnsiStyle.removed).flatMap(_ split "\n").maxBy(_.size).size max longestLineInHeaderWithoutStyles
      }

      lazy val hyphens = "-" * (numberOfCharsInTheLongestLine min availableWidthInCharacters)

      def center(in: String): String = {
        val emptySpace = hyphens.size - AnsiStyle.removed(in).size
        val leftPadding = " " * (emptySpace / 2)

        leftPadding + in
      }

      val centeredHeader =
        header.wrappedOnSpaces(availableWidthInCharacters)
          .split("\n")
          .toList
          .map(center)
          .mkString("\n")

      val metaContentPartOfTable = table.dropRight(1)
      val valuePartOfTable = if (metaContentPartOfTable.isEmpty) Vector(table.last) else Vector(hyphens, table.last)

      val resultingLines = Vector(hyphens, centeredHeader, hyphens) ++ metaContentPartOfTable ++ valuePartOfTable :+ hyphens

      styled(resultingLines mkString "\n")(style)
    }
  }

  private[spells] object XrayReport {
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
