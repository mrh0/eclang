package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.atom.AtomInstance

object EcTypeAtom : EcType("Atom") {
    override fun isReferenceType() = true
}