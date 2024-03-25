package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

abstract class EcType(val namespace: String, val identifier: String) {
    override fun toString(): String {
        return "$namespace.$identifier"
    }

    open fun accepts(location: Loc, type: EcType): Boolean = true

    open fun cast(location: Loc, to: EcType): EcType {
        if(!this.accepts(location, to)) throw EcError(location, "Cannot cast $this to $to")
        return to
    }

    open fun expand(): List<EcType> = listOf(this)
}