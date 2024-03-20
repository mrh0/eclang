package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeAtom : EcTypeAny("Gs", "Atom") {
    override fun accepts(location: Loc, type: EcTypeAny): Boolean = type == this
}