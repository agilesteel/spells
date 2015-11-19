package spells

trait FunctionNOpsModule {
  this: CustomRenderingModule with SpellsConfigModule with StringOpsModule =>

  import scala.reflect.runtime.universe._

  implicit class Function1OpsFromSpells[T1, R, F[_, _]](value: F[T1, R])(implicit typeTag: TypeTag[F[T1, R]], evidence: F[T1, R] <:< Function1[T1, R]) extends CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      typeTag.tpe.toString.withDecodedScalaSymbols
  }

  implicit class Function2OpsFromSpells[T1, T2, R, F[_, _, _]](value: F[T1, T2, R])(implicit typeTag: TypeTag[F[T1, T2, R]], evidence: F[T1, T2, R] <:< Function2[T1, T2, R]) extends CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      typeTag.tpe.toString.withDecodedScalaSymbols
  }
}
