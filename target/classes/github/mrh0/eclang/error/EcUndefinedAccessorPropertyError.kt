package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcUndefinedAccessorPropertyError (location: Loc, name: String, type: Type)
    : EcError(location, "Cannot access property '$name' of type $type") {
}