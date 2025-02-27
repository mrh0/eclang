package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypePointer(val wrapped: EcType) : EcType("pointer($wrapped)") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypePointer) wrapped.accepts(location, type.wrapped) else false

    override fun isReferenceType(): Boolean = true
}