package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypeNullable(val wrapped: EcType) : EcType("nullable($wrapped)") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeNullable) wrapped.accepts(location, type.wrapped) else if(type is EcTypeNull) true else wrapped.accepts(location, type)

    override fun isReferenceType(): Boolean = wrapped.isReferenceType() // nullable must be ref type
}