package spells

trait DurationOps {
  this: HumanRendering =>

  implicit class DurationOpsFromSpells(value: scala.concurrent.duration.Duration) extends CustomRendering {
    override final def rendered: String = value.toNanos.render.duration.nanoseconds
  }
}
