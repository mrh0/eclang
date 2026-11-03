package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcAssignConstError (location: Loc, name: String)
    : EcError(location, "Cannot assign a new value to constant '$name'") {
}