package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcTypeAny

class EcAssignTypeError (location: Loc, name: String, type: EcTypeAny, otherType: EcTypeAny)
    : EcError(location, "Cannot set variable '$name' of type $type to value of type $otherType") {
}