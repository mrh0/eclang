package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeNone : EcType("Ec", "None") {
    override fun accepts(location: Loc, type: EcType): Boolean = false
}