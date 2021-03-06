package spells

/** Provides utilities for all kinds of `Iterable`s including Java collections and `Array`s. */
trait IterableOpsModule {
  this: AnsiModule
    with AnyOpsModule
    with CalendarOpsModule
    with CustomRenderingModule
    with DurationOpsModule
    with HumanRenderingModule
    with SpellsConfigModule
    with StringOpsModule
    with StylePrintModule
    with Tuple2OpsModule =>

  import scala.reflect.runtime.universe._

  final implicit def IterableOpsFromSpells[A, T[A] <: Iterable[A]](
      value: T[A]
    )(implicit
      typeTag: TypeTag[T[A]],
      rendering: A => CustomRenderingModule#CustomRendering =
        CustomRendering.Defaults.Any
    ): CustomRendering =
    new CustomRendering {
      final override def rendered(
          implicit
          availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
            SpellsConfig.terminal.WidthInCharacters.value
        ): String = {
        lazy val typeName = typeTag.tpe.toString.withDecodedScalaSymbols

        render[T[A]](
          value,
          _.isEmpty,
          _.size,
          value.toString,
          typeName,
          availableWidthInCharacters
        ) { _ => availableWidthInCharacters =>
          var result: Vector[(String, String)] =
            Vector.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result :+= (index.toString -> element.rendered(
              availableWidthInCharacters
            ))
            index += 1
          }

          result
        }
      }
    }

  final implicit def MapOpsFromSpells[
      Key,
      Value,
      T[Key, Value] <: Map[Key, Value]
    ](
      value: T[Key, Value]
    )(implicit
      typeTag: TypeTag[T[Key, Value]],
      rendering: Tuple2[Key, Value] => CustomRenderingModule#CustomRendering =
        CustomRendering.Defaults.Any
    ): CustomRendering =
    new CustomRendering {
      final override def rendered(
          implicit
          availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
            SpellsConfig.terminal.WidthInCharacters.value
        ): String = {
        lazy val typeName = typeTag.tpe.toString.withDecodedScalaSymbols

        render[T[Key, Value]](
          value,
          _.isEmpty,
          _.size,
          value.toString,
          typeName,
          availableWidthInCharacters
        ) { _ => availableWidthInCharacters =>
          var result: Vector[(String, String)] =
            Vector.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result :+= (index.toString -> element.rendered(
              availableWidthInCharacters
            ))
            index += 1
          }

          result
        }
      }
    }

  final implicit def ArrayOpsFromSpells[A, T[A] <: Array[A]](
      value: T[A]
    )(implicit
      typeTag: TypeTag[T[A]],
      rendering: A => CustomRenderingModule#CustomRendering =
        CustomRendering.Defaults.Any
    ): CustomRendering =
    new CustomRendering {
      final override def rendered(
          implicit
          availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
            SpellsConfig.terminal.WidthInCharacters.value
        ): String = {
        lazy val typeName = typeTag.tpe.toString.withDecodedScalaSymbols

        render[T[A]](
          value,
          _.isEmpty,
          _.size,
          s"Array()",
          typeName,
          availableWidthInCharacters
        ) { _ => availableWidthInCharacters =>
          var result: Vector[(String, String)] =
            Vector.empty[(String, String)]
          var index = 0
          value foreach { element =>
            result :+= (index.toString -> element.rendered(
              availableWidthInCharacters
            ))
            index += 1
          }

          result
        }
      }
    }

  final implicit def CollectionOpsFromSpells[
      A,
      T[A] <: java.util.Collection[A]
    ](
      value: T[A]
    )(implicit
      typeTag: TypeTag[T[A]],
      rendering: A => CustomRenderingModule#CustomRendering =
        CustomRendering.Defaults.Any
    ): CustomRendering =
    new CustomRendering {
      final override def rendered(
          implicit
          availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
            SpellsConfig.terminal.WidthInCharacters.value
        ): String = {
        lazy val className = value.decodedClassName
        lazy val typeName = typeTag.tpe.toString.withDecodedScalaSymbols

        render[T[A]](
          value,
          _.isEmpty,
          _.size,
          s"$className()",
          typeName,
          availableWidthInCharacters
        ) { _ => availableWidthInCharacters =>
          var result: Vector[(String, String)] =
            Vector.empty[(String, String)]
          var index = 0
          val iterator = value.iterator
          while (iterator.hasNext) {
            val element = iterator.next
            result :+= (index.toString -> element.rendered(
              availableWidthInCharacters
            ))
            index += 1
          }

          result
        }
      }
    }

  final implicit def JavaMapOpsFromSpells[
      Key,
      Value,
      T[Key, Value] <: java.util.Map[Key, Value]
    ](
      value: T[Key, Value]
    )(implicit
      typeTag: TypeTag[T[Key, Value]],
      rendering: java
        .util
        .Map.Entry[Key, Value] => CustomRenderingModule#CustomRendering =
        CustomRendering.Defaults.Any
    ): CustomRendering =
    new CustomRendering {
      final override def rendered(
          implicit
          availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
            SpellsConfig.terminal.WidthInCharacters.value
        ): String = {
        lazy val className = value.decodedClassName
        lazy val typeName = typeTag.tpe.toString.withDecodedScalaSymbols

        render[java.util.Map[Key, Value]](
          value,
          _.isEmpty,
          _.size,
          s"$className()",
          typeName,
          availableWidthInCharacters
        ) { in => availableWidthInCharacters =>
          var result: Vector[(String, String)] =
            Vector.empty[(String, String)]
          var index = 0
          val iterator = in.entrySet.iterator
          while (iterator.hasNext) {
            val element = iterator.next
            result :+= (index.toString -> element.rendered(
              availableWidthInCharacters
            ))
            index += 1
          }

          result
        }
      }
    }

  final private def render[T](
      value: => T,
      isEmpty: T => Boolean,
      getSize: T => Int,
      emptyRendered: => String,
      typeName: => String,
      availableWidthInCharacters: Int
    )(
      pairs: T => Int => Seq[(String, String)]
    ): String = {
    def nonEmptyRendered: String = {
      val size = getSize(value)
      val sizeString = if (size == 1) "1 element" else s"$size elements"

      s"$typeName with $sizeString" + ":\n\n" + renderedTable(
        pairs(value),
        availableWidthInCharacters
      ).mkString("\n")
    }

    if (isEmpty(value)) emptyRendered else nonEmptyRendered
  }

  final private[spells] def renderedTable(
      in: Int => Seq[(String, String)],
      availableWidthInCharacters: Int
    ): Seq[String] =
    if (in(0).isEmpty) Seq.empty
    else {
      val sizeOfTheBiggestKey = in(0) map {
        case (key, _) => AnsiStyle.removed(key).size
      } max

      val separator = " │ "

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
              (head :: subLines
                .tail
                .map(subLine =>
                  (" " * keyWithPadding.size) + separator + subLine
                )).mkString("\n")
            }
          }

          result :+ line
      }
    }
}
