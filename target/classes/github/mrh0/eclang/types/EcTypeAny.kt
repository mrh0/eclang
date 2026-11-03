package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeAny : EcType("Any", EcTypeNone) {
    override fun accepts(location: Loc, type: EcType): Boolean = true
}