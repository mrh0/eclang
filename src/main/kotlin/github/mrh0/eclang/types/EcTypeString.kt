package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeString : EcTypeAny("Gs", "String") {
    override fun accepts(location: Loc, type: EcTypeAny): Boolean = type == this
}