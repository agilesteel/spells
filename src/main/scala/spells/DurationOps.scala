package spells

import scala.concurrent.duration._

trait DurationOps {
  this: HumanRendering =>

  implicit class DurationOpsFromSpells(value: Duration) extends CustomRendering {
    def rendered: String = value.toNanos.render.duration.nanoseconds
  }
}
