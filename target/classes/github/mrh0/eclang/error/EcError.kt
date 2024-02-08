package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

open class EcError(val location: Loc, message: String) : Exception("$location: $message") {
}