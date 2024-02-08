package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

abstract class EcTypeAny(val namespace: String, val identifier: String) {
    override fun toString(): String {
        return "$namespace.$identifier"
    }

    open fun accepts(location: Loc, type: EcTypeAny): Boolean = type == this

    open fun cast(location: Loc, to: EcTypeAny): EcTypeAny {
        if(!this.accepts(location, to)) throw EcError(location, "Cannot cast $this to $to")
        return to
    }
}