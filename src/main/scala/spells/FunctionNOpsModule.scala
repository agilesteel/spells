package spells

/** Provides custom rendering for the FunctionN traits. */
trait FunctionNOpsModule {
  this: CustomRenderingModule with SpellsConfigModule with StringOpsModule =>

  import scala.reflect.runtime.universe._

  abstract class TypeTagBasedCustomRendering(typeTag: TypeTag[_])
      extends CustomRendering {
    override def rendered(
        implicit
        availableWidthInCharacters: StringOpsModule#AvailableWidthInCharacters =
          SpellsConfig.terminal.WidthInCharacters.value
      ): String =
      typeTag.tpe.toString.withDecodedScalaSymbols
  }

  final implicit class Function0OpsFromSpells[R, F[_]](
      value: F[R]
    )(implicit
      typeTag: TypeTag[F[R]],
      evidence: F[R] <:< Function0[R]
    ) extends TypeTagBasedCustomRendering(typeTag)

  // final implicit class Function1OpsFromSpells[T1, R](
  //     value: Function1[T1, R]
  //   )(
  //     implicit
  //     typeTag: TypeTag[Function1[T1, R]])
  //     extends TypeTagBasedCustomRendering(typeTag)

  final implicit class Function2OpsFromSpells[T1, T2, R, F[_, _, _]](
      value: F[T1, T2, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, R]],
      evidence: F[T1, T2, R] <:< Function2[T1, T2, R]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function3OpsFromSpells[T1, T2, T3, R, F[_, _, _, _]](
      value: F[T1, T2, T3, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, R]],
      evidence: F[T1, T2, T3, R] <:< Function3[T1, T2, T3, R]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function4OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      R,
      F[_, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, R]],
      evidence: F[T1, T2, T3, T4, R] <:< Function4[T1, T2, T3, T4, R]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function5OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      R,
      F[_, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, R]],
      evidence: F[T1, T2, T3, T4, T5, R] <:< Function5[T1, T2, T3, T4, T5, R]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function6OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      R,
      F[_, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, R]],
      evidence: F[T1, T2, T3, T4, T5, T6, R] <:< Function6[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function7OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      R,
      F[_, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, R]],
      evidence: F[T1, T2, T3, T4, T5, T6, T7, R] <:< Function7[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function8OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      R,
      F[_, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, R]],
      evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, R] <:< Function8[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function9OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      R,
      F[_, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R]],
      evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, R] <:< Function9[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function10OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R]],
      evidence: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R] <:< Function10[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function11OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        R
      ] <:< Function11[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function12OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]
    )(implicit
      typeTag: TypeTag[F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        R
      ] <:< Function12[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function13OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]
    )(implicit
      typeTag: TypeTag[
        F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R]
      ],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        R
      ] <:< Function13[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function14OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]
    )(implicit
      typeTag: TypeTag[
        F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R]
      ],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        R
      ] <:< Function14[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function15OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        R
      ]
    )(implicit
      typeTag: TypeTag[
        F[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R]
      ],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        R
      ] <:< Function15[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function16OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        R
      ] <:< Function16[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function17OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        R
      ] <:< Function17[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function18OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      T18,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        R
      ] <:< Function18[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function19OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      T18,
      T19,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        R
      ] <:< Function19[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function20OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      T18,
      T19,
      T20,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        R
      ] <:< Function20[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function21OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      T18,
      T19,
      T20,
      T21,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        R
      ] <:< Function21[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
  final implicit class Function22OpsFromSpells[
      T1,
      T2,
      T3,
      T4,
      T5,
      T6,
      T7,
      T8,
      T9,
      T10,
      T11,
      T12,
      T13,
      T14,
      T15,
      T16,
      T17,
      T18,
      T19,
      T20,
      T21,
      T22,
      R,
      F[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
    ](
      value: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        R
      ]
    )(implicit
      typeTag: TypeTag[F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        R
      ]],
      evidence: F[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        R
      ] <:< Function22[
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        T7,
        T8,
        T9,
        T10,
        T11,
        T12,
        T13,
        T14,
        T15,
        T16,
        T17,
        T18,
        T19,
        T20,
        T21,
        T22,
        R
      ]
    ) extends TypeTagBasedCustomRendering(typeTag)
}
