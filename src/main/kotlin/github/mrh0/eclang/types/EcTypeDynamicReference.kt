package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeDynamicReference(val wrapped: EcType) : EcType("DR(${wrapped})") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeDynamicReference) wrapped.accepts(location, type.wrapped) else wrapped.accepts(location, type)

    // override fun expand(): List<EcType> = listOf(wrapped, EcTypeNull)
}