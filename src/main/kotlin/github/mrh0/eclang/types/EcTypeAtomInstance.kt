package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.atom.AtomInstance

data class EcTypeAtomInstance(val atom: AtomInstance) : EcType("Atom(:${atom.label})") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeAtomInstance) type.atom == atom else false

    override fun isReferenceType() = true
}