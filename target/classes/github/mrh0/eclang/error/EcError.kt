package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc

/** A compile error in the user's program, reported as `file:line:column: error: reason`. */
open class EcError(val location: Loc, val reason: String) : Exception("$location: $reason") {
}
