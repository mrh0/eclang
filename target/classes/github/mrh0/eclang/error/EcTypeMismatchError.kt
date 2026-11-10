package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

/** A value of type [got] was used where [expected] is required. [reason] says why it doesn't fit and how to fix it. */
class EcTypeMismatchError(location: Loc, expected: Type, got: Type, what: String, reason: String)
    : EcError(location, "Cannot use $got as $expected ($what): $reason") {
}
