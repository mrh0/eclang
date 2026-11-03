package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

class EcFunctionNotDefinedError (location: Loc, name: String, namespace: String) : EcError(location, "Function '$name' is not defined in module '$namespace'") {
}