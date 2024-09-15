package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcUndefinedAccessorPropertyError (location: Loc, name: String, type: EcType)
    : EcError(location, "Cannot access property '$name' of type $type") {
}