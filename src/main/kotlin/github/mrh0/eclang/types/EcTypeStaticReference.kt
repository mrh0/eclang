package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc

class EcTypeStaticReference(val wrapped: EcType) : EcType("SR(${wrapped})") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeStaticReference) wrapped.accepts(location, type.wrapped) else wrapped.accepts(location, type)

    // override fun expand(): List<EcType> = listOf(wrapped, EcTypeNull)
}