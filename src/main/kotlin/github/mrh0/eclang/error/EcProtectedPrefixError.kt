package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

open class EcProtectedPrefixError(val location: Loc) : Exception("$location: Identifier cannot start with protected prefix '__ec_'.") {
}