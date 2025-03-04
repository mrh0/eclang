package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcUndefinedAccessorPropertyError

abstract class EcType(val identifier: String) {
    override fun toString(): String = identifier

    open fun accepts(location: Loc, type: EcType): Boolean = type == this

    open fun cast(location: Loc, to: EcType): EcType {
        if(!this.accepts(location, to)) throw EcError(location, "Cannot cast $this to $to")
        return to
    }

    open fun expand(): List<EcType> = listOf(this)

    open fun getNamespace() = "EC"

    open fun isReferenceType() = false

    open fun canBeInstantiated() = true

    open fun getProperty(location: Loc, name: String): EcType = throw EcUndefinedAccessorPropertyError(location, name, this)

    open fun accessor(location: Loc, indexType: EcType): EcType = throw EcUndefinedAccessorPropertyError(location, "[$indexType]", this)
}