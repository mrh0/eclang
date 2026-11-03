package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

open class EcNotReferenceTypeError(location: Loc, type: EcType) : EcError(location, "Type '$type' is not a reference type") {
}