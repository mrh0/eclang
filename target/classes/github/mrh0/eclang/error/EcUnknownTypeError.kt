package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcUnknownTypeError(location: Loc, type: String) : EcError(location, "Unknown type named '$type'.") {
}