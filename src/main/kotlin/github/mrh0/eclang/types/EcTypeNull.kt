package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeNull : EcType("Null") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this
}