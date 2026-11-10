package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcUnknownUnitTypeError(location: Loc, name: String, type: Type) : EcError(location, "Unknown unit '$name' on type '$type'.") {
}