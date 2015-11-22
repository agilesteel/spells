package spells

trait DurationOpsModule {
  this: CustomRenderingModule with HumanRenderingModule =>

  implicit final class DurationOpsFromSpells(value: scala.concurrent.duration.Duration) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      value.toNanos.render.duration.nanoseconds
  }
}
