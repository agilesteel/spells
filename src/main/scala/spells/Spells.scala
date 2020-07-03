package spells

/** The main spells module.
  * Mix it into the highest level `package object` you can afford,
  * in order to gain the most benefit from spells.
  */
trait Spells
    extends AnsiModule
       with AnyOpsModule
       with CalendarOpsModule
       with ClearPrintModule
       with ClipboardModule
       with CustomRenderingModule
       with DateOpsModule
       with DurationOpsModule
       with FunctionNOpsModule
       with HumanRenderingModule
       with IterableOpsModule
       with LazyListOpsModule
       with MiscModule
       with SpellsConfigModule
       with StackTraceElementModule
       with StringOpsModule
       with StylePrintModule
       with ThrowableOpsModule
       with Tuple2OpsModule
       with XrayModule
