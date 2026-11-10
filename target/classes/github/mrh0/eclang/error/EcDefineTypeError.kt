package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcDefineTypeError (location: Loc, name: String, varType: Type, toType: Type)
    : EcError(location, "Cannot define variable '$name' of type $varType to value of type $toType") {
}