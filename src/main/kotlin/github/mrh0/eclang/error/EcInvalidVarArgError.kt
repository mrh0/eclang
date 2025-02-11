package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcInvalidVarArgError(location: Loc, name: String) : EcError(location, "Variable length function parameter must be last in function signature.") {
}