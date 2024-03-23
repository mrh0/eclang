package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeAny : EcType("Ec", "Any") {
    override fun accepts(location: Loc, type: EcType): Boolean = true
}