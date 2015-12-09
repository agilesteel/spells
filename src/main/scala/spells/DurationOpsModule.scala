package spells

/** Provides utility methods for scala.concurrent.duration.Duration. */
trait DurationOpsModule {
  this: CustomRenderingModule with HumanRenderingModule =>

  import scala.concurrent.duration.Duration

  implicit final class DurationOpsFromSpells(value: Duration) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      value.toNanos.render.duration.nanoseconds
  }
}
