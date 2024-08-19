package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcReturnTypeError(location: Loc, expected: EcType, got: EcType)
    : EcError(location, "Expected return type $expected got $got.") {
}