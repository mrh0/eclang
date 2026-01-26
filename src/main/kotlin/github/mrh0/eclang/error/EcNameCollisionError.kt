package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcNameCollisionError (location: Loc, name: String)
    : EcError(location, "Source name '$name' is already taken") {
}