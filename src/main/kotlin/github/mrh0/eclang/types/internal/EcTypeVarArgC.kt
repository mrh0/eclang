package github.mrh0.eclang.types.internal

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

object EcTypeVarArgC : EcType("VarArgC") {
    override fun toString() = "VA_C"
    override fun accepts(location: Loc, type: EcType): Boolean = true

    override fun isReferenceType(): Boolean = false
}