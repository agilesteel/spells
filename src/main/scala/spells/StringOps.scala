package spells

trait StringOps {
  implicit class StringOpsFromSpells(input: String) {
    def withDecodedScalaSymbols: String = scala.reflect.NameTransformer decode input

    def wrappedOnSpaces(limit: Int): String = { // and tabs?
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

      def wouldOverflow(line: String, atom: String): Boolean =
        (line + separator + atom).size > limit

      def brokenCurrentLineWithAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        (result + line + "\n") -> atom

      def justAtomCarriedOverToNextLine(result: String, line: String, atom: String): (String, String) =
        result -> (line + separator + atom)

      if (input.size < limit || !(input contains ' ')) input else {
        val parts = input split "\n" map wrapped

        parts.tail.foldLeft(parts.head) {
          case (result, currentPart) =>
            if (result endsWith "\n")
              result + currentPart
            else result + "\n" + currentPart
        }
      }
    }
  }
}
