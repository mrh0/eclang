package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypeBoxed(val wrapped: EcType) : EcType("Boxed($wrapped)") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeBoxed) wrapped.accepts(location, type.wrapped) else false

    override fun isReferenceType(): Boolean = true
}