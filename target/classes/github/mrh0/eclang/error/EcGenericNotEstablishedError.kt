package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcGenericNotEstablishedError (location: Loc, name: String) : EcError(location, "Generic return type '$name' was never established") {
}