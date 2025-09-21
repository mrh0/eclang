package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypeGeneric(val name: String) : EcType("<$name>") {
    override fun accepts(location: Loc, type: EcType): Boolean = true

    override fun isReferenceType(): Boolean = true

    override fun onNew() {}
}