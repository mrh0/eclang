package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.ect2.Type

open class EcNotReferenceTypeError(location: Loc, type: Type) : EcError(location, "Type '$type' is not a reference type") {
}