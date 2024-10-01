package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcUnexpectedTypeError(location: Loc, expected: EcType, got: EcType)
    : EcError(location, "Expected type '$expected' type '$got'.") {
}