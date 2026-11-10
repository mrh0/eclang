package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcUnexpectedTypeError(location: Loc, expected: Type, got: Type)
    : EcError(location, "Expected type '$expected' got type '$got'.") {
}