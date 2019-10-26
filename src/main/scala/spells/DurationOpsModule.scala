package spells

/** Provides utility methods for scala.concurrent.duration.Duration. */
trait DurationOpsModule {
  this: CustomRenderingModule
    with HumanRenderingModule
    with StringOpsModule
    with SpellsConfigModule =>

  import scala.concurrent.duration.Duration

  final implicit class DurationOpsFromSpells(value: Duration)
      extends CustomRendering {
    final override def rendered(
        implicit
        availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String =
      value.toNanos.render.duration.nanoseconds
  }
}
