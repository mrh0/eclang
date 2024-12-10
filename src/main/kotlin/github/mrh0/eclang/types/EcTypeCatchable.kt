package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

data class EcTypeCatchable(val returnedType: EcType, val throwableType: EcType) : EcTypeTuple(arrayOf(returnedType, throwableType)) {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = true
}