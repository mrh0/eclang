package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeNull : EcType("Null", EcTypePointer.of(EcTypeNone)) {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType(): Boolean = true
}