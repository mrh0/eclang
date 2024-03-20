package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeNone : EcTypeAny("Gs", "None") {
    override fun accepts(location: Loc, type: EcTypeAny): Boolean = false
}