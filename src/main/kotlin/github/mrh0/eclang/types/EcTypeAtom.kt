package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

object EcTypeAtom : EcType("Ec", "Atom") {
    override fun accepts(location: Loc, type: EcType): Boolean = type == this
}