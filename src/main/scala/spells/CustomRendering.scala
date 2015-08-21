package spells

trait CustomRendering {
  implicit def rendered: String
}

object CustomRendering {
  private[spells] object Default extends (Any => CustomRendering) {
    def apply(any: Any): CustomRendering = new CustomRendering {
      def rendered: String = String valueOf any
    }
  }
}
