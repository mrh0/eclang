package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcAssignTypeError (location: Loc, name: String, type: EcType, otherType: EcType)
    : EcError(location, "Cannot set variable '$name' of type $type to value of type $otherType") {
}