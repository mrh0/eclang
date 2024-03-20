package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeBool : EcTypeAny("Gs", "Bool") {
    override fun accepts(location: Loc, type: EcTypeAny): Boolean = type == this
}