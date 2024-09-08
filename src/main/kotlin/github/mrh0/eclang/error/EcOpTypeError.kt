package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcOpTypeError(location: Loc, op: String, vararg types: EcType)
    : EcError(location, "$op is not compatible with type${ if (types.size > 1) "s" else "" } ${types.joinToString { it.identifier }}.") {
}