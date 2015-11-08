package spells

trait DurationOpsModule {
  this: HumanRenderingModule =>

  implicit class DurationOpsFromSpells(value: scala.concurrent.duration.Duration) extends CustomRendering {
    override final def rendered(implicit availableWidthInCharacters: CustomRendering.AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      value.toNanos.render.duration.nanoseconds
  }
}
