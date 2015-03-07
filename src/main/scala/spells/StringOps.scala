package spells

trait StringOps {
  implicit class StringOps(input: String) {
    def withDecodedScalaSymbols: String = scala.reflect.NameTransformer decode input

    def wrapOnSpaces(limit: Int): String = {
      def wrapped: String = {
        val separator = " "
        val atoms = input split separator

        val (result, line) = atoms.tail.foldLeft(("", atoms.head)) {
          case ((result, line), atom) =>
            if ((line + separator + atom).size > limit)
              (result + line + "\n") -> atom
            else
              result -> (line + separator + atom)
        }

        result + line
      }

      if (input.size < limit || !(input contains " ")) input else wrapped
    }
  }
}
