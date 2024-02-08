package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny

class EcOpTypeError(location: Loc, op: String, vararg types: EcTypeAny)
    : EcError(location, "$op is not compatible with type${ if (types.size > 1) "s" else "" } $types.") {
}