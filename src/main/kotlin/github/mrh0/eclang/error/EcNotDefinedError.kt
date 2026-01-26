package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcNotDefinedError (location: Loc, name: String) : EcError(location, "Value '$name' is not defined") {
}