package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcAssignConstError (location: Loc, name: String, kind: String)
    : EcError(location, "'$name' is $kind, so it can't be assigned a new value. " +
        if (kind == "a parameter") "Copy it into a `var` to change it here, or make it a `mut` parameter (`mut $name: ...`) to change the caller's variable." else "To change it, use a `var` instead.") {
}