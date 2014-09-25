package spells

trait XRay {
  implicit def anyToXRay[T](value: T): XRay.XRay[T] = new XRay.XRay(value)
}

object XRay {
  class XRay[T](value: => T) {
    def xray(): T = {
      val frozenValue = value
      Console.println(frozenValue.magenta)
      frozenValue
    }
  }
}
