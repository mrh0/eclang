package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

data class EcTypeTuple(val returnType: EcType, val thrownType: EcType) : EcType("Tuple") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = true
}