package spells

/** Provides utilities for `String`s */
trait StringOpsModule {
  implicit final class StringOpsFromSpells(input: String) {
    /** Decodes special symbols scalac is using in generated names.
      * {{{
      * class `Encoded + Fancy` {
      * def show(): Unit = {
      *  println(getClass.getName)                        // Encoded$u0020$plus$u0020Fancy
      *  println(getClass.getName.withDecodedScalaSymbols // Encoded + Fancy
      *  println(this.decodedClassName)                   // Encoded + Fancy
      * }
      * }
      * }}}
      * @return a decoded `String`
      */
    final def withDecodedScalaSymbols: String = scala.reflect.NameTransformer decode input

    /** Line wrapping.
      * {{{
      *  "hello world".wrappedOnSpaces(5) // "hello\nworld"
      * }}}
      * @param limitInCharacters the maximum length
      * @return
      */
    final def wrappedOnSpaces(limitInCharacters: Int): String = {
      val separator = " "

      def wrapped(in: String): String = {
        val atoms = in split separator

        val (result, lastLine) =
          atoms.tail.foldLeft(("", atoms.head)) {
            case ((result, line), atom) =>
              if (wouldOverflow(line, atom))
                brokenCurrentLineWithAtomCarriedOverToNextLine(result, line, atom)
              else
                justAtomCarriedOverToNextLine(result, line, atom)
          }

        result + lastLine
      }

      def wouldOverflow(line: String, atom: String): Boolean = {
        if (atom.isEmpty) line.size > limitInCharacters
        else (line + separator + atom).size > limitInCharacters
      }

      def brokenCurrentLineWithAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        (result + line + "\n") -> atom

      def justAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        result -> (line + separator + atom)

      if (input.size < limitInCharacters || !(input contains ' ')) input else {
        val parts = input split "\n" map wrapped

        parts.tail.foldLeft(parts.head) {
          case (result, currentPart) =>
            val current = if (currentPart.isEmpty) "\n" else currentPart

            if (result.endsWith("\n"))
              result + current
            else
              result + "\n" + current
        }
      }
    }
  }
}
