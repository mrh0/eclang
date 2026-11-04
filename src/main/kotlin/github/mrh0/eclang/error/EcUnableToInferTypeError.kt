package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

class EcUnableToInferTypeError(location: Loc) : EcError(location, "Unable to infer type.") {
}