package spells.user

object SilentOutputStream extends java.io.OutputStream {
  override def write(b: Int): Unit = {}

  def out(block: => Unit): Unit =
    Console.withOut(this)(block)

  def err(block: => Unit): Unit =
    Console.withErr(this)(block)
}
