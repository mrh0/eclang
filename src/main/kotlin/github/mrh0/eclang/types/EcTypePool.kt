package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypePool : EcType("Pool") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = true
}