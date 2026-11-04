package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.ect2.Type

class EcReturnTypeError(location: Loc, expected: Type, got: Type)
    : EcError(location, "Expected return type $expected got $got.") {
}