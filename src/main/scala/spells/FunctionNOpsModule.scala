package spells

trait FunctionNOpsModule {
  this: CustomRenderingModule with SpellsConfigModule with StringOpsModule =>

  import scala.reflect.runtime.universe._

  abstract class TypeTagBasedCustomRendering(typeTag: TypeTag[_]) extends CustomRendering {
    def rendered(implicit availableWidthInCharacters: CustomRenderingModule#AvailableWidthInCharacters = CustomRendering.Defaults.AvailableWidthInCharacters): String =
      typeTag.tpe.toString.withDecodedScalaSymbols
  }

  implicit class Function1OpsFromSpells[T1, R, F[_, _]](value: F[T1, R])(implicit typeTag: TypeTag[F[T1, R]], evidence: F[T1, R] <:< Function1[T1, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function2OpsFromSpells[T1, T2, R, F[_, _, _]](value: F[T1, T2, R])(implicit typeTag: TypeTag[F[T1, T2, R]], evidence: F[T1, T2, R] <:< Function2[T1, T2, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function3OpsFromSpells[T1, T2, T3, R, F[_, _, _, _]](value: F[T1, T2, T3, R])(implicit typeTag: TypeTag[F[T1, T2, T3, R]], evidence: F[T1, T2, T3, R] <:< Function3[T1, T2, T3, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function4OpsFromSpells[T1, T2, T3, T4, R, F[_, _, _, _, _]](value: F[T1, T2, T3, T4, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, R]], evidence: F[T1, T2, T3, T4, R] <:< Function4[T1, T2, T3, T4, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function5OpsFromSpells[T1, T2, T3, T4, T5, R, F[_, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, R]], evidence: F[T1, T2, T3, T4, T5, R] <:< Function5[T1, T2, T3, T4, T5, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function6OpsFromSpells[T1, T2, T3, T4, T5, T6, R, F[_, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, R]], evidence: F[T1, T2, T3, T4, T5, T6, R] <:< Function6[T1, T2, T3, T4, T5, T6, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function7OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, R, F[_, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, R] <:< Function7[T1, T2, T3, T4, T5, T6, T7, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function8OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, T8, R, F[_, _, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, T8, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, R] <:< Function8[T1, T2, T3, T4, T5, T6, T7, T8, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function9OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, T8, T9, R, F[_, _, _, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] <:< Function9[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function10OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R, F[_, _, _, _, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] <:< Function10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function11OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R, F[_, _, _, _, _, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R] <:< Function11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]) extends TypeTagBasedCustomRendering(typeTag)
  implicit class Function12OpsFromSpells[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R, F[_, _, _, _, _, _, _, _, _, _, _, _, _]](value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R])(implicit typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]], evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R] <:< Function12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]) extends TypeTagBasedCustomRendering(typeTag)
}
