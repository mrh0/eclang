package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcUnableToInferTypeError(location: Loc) : EcError(location, "Unable to infer type.") {
}