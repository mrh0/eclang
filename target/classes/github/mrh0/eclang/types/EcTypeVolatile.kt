package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

data class EcTypeVolatile(val wrapped: EcType) : EcType("volatile($wrapped)", wrapped.inherits, wrapped.namespace) {
    override fun accepts(location: Loc, type: EcType): Boolean = wrapped.accepts(location, type)

    override fun isReferenceType(): Boolean = wrapped.isReferenceType()

    override fun accessor(location: Loc, indexType: EcType): EcType = wrapped.accessor(location, indexType)

    override fun getProperty(location: Loc, name: String): EcType = wrapped.getProperty(location, name)

    override fun canBeInstantiated(): Boolean = wrapped.canBeInstantiated()
}