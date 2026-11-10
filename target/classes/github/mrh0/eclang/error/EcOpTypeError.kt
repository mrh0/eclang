package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcOpTypeError(location: Loc, op: String, vararg types: Type)
    : EcError(location, "$op is not compatible with type${ if (types.size > 1) "s" else "" } ${types.joinToString { it.toString() }}.") {
}