package spells

/** Provides utilities for `String`s */
trait StringOpsModule {
  this: AnsiModule =>

  implicit final class StringOpsFromSpells(input: String) {
    /** Decodes special symbols scalac is using in generated names.
      * {{{
      * class `Encoded + Fancy` {
      * def show(): Unit = {
      * println(getClass.getName)                        // Encoded$u0020$plus$u0020Fancy
      * println(getClass.getName.withDecodedScalaSymbols // Encoded + Fancy
      * println(this.decodedClassName)                   // Encoded + Fancy
      * }
      * }
      * }}}
      * @return a decoded `String`
      */
    final def withDecodedScalaSymbols: String =
      try scala.reflect.NameTransformer decode input
      catch {
        // $COVERAGE-OFF$
        case bug: Exception => input
        // $COVERAGE-ON$
      }

    /** Line wrapping.
      * {{{
      * "hello world".wrappedOnSpaces(5) // "hello\nworld"
      * }}}
      * @param limitInCharacters the maximum length
      * @return
      */
    final def wrappedOnSpaces(limitInCharacters: Int): String = {
      val separator = " "

      def wrapped(in: String): String = {
        in.split(separator).toList match {
          case head :: tail =>
            val (result, lastLine) =
              tail.foldLeft(("", head)) {
                case ((result, line), atom) =>
                  if (wouldOverflow(line, atom))
                    brokenCurrentLineWithAtomCarriedOverToNextLine(result, line, atom)
                  else
                    justAtomCarriedOverToNextLine(result, line, atom)
              }

            result + lastLine

          case _ => in
        }
      }

      def wouldOverflow(line: String, atom: String): Boolean = {
        if (atom.isEmpty) AnsiStyle.removed(line).size > limitInCharacters
        else AnsiStyle.removed(line + separator + atom).size > limitInCharacters
      }

      def brokenCurrentLineWithAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        (result + line + "\n") -> atom

      def justAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        result -> (line + separator + atom)

      input.split("\n").toList.map(wrapped) match {
        case head :: tail =>
          tail.foldLeft(head) {
            case (result, currentPart) =>
              val current = if (currentPart.isEmpty) "\n" else currentPart

              if (result.endsWith("\n"))
                result + current
              else
                result + "\n" + current
          }
        case _ => input
      }
    }
  }
}
