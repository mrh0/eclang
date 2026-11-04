package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcUnknownTypeError(location: Loc, type: String, suggestion: String = "") : EcError(location, "Unknown type named '$type'.$suggestion") {
}
