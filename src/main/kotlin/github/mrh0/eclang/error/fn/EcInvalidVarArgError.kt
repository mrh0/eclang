package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError

class EcInvalidVarArgError(location: Loc, name: String) : EcError(location, "Variable length function parameter must be last in function signature.") {
}