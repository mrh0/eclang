package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.atom.AtomInstance

class EcTypeAtom(val atom: AtomInstance) : EcType("Ec", "Atom") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeAtom) type.atom == atom else false
}