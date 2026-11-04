package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcAssignConstError (location: Loc, name: String, kind: String)
    : EcError(location, "'$name' is $kind, so it can't be assigned a new value. To change it, use a `var` instead.") {
}