package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

class EcTypeNullable(val wrapped: EcType) : EcType("Nullable($wrapped)") {
    override fun accepts(location: Loc, type: EcType): Boolean = if (type is EcTypeNullable) wrapped.accepts(location, type.wrapped) else wrapped.accepts(location, type)
}