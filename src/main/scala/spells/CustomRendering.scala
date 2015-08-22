package spells

trait CustomRendering {
  implicit def rendered: String
}

object CustomRendering {
  object Defaults {
    object Any extends (Any => CustomRendering) {
      def apply(any: Any): CustomRendering = new CustomRendering {
        def rendered: String = String valueOf any
      }
    }
  }
}
