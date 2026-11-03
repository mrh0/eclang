package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcUnknownUnitTypeError(location: Loc, name: String, type: EcType) : EcError(location, "Unknown unit '$name' on type '$type'.") {
}