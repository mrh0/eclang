package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.atom.AtomInstance

class EcTypeAtomInstance private constructor(val atom: AtomInstance) : EcType(getAtomId(atom), EcTypeAtom) {
    companion object {
        fun getAtomId(atom: AtomInstance) = "Atom(${atom.label})"

        fun of(atom: AtomInstance) = ALL_TYPES.getOrPut(getAtomId(atom)) { EcTypeAtomInstance(atom) }
    }

    override fun accepts(location: Loc, type: EcType): Boolean = type == this

    override fun isReferenceType() = false // Could be either
}