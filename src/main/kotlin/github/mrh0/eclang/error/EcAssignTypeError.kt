package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcAssignTypeError (location: Loc, name: String, varType: EcType, toType: EcType)
    : EcError(location, "Cannot assign variable '$name' of type $varType to value of type $toType") {
}