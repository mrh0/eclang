package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.atom.AtomInstance

object EcTypeAtom : EcType("Ec", "Atom") {
    override fun accepts(location: Loc, type: EcType): Boolean = type is EcTypeAtomInstance || type == this
}