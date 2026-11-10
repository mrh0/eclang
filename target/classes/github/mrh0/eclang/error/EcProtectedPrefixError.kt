package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

class EcProtectedPrefixError(location: Loc) : EcError(location, "Identifier cannot start with protected prefix '__ec_'.") {
}
