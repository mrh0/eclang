package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcAmbiguousSignatureError (location: Loc, name: String, argTypes: Array<EcType>, locations: List<Loc>) : EcError(location, "Ambiguous call signature for $name(${argTypes.joinToString(",")}) with ${locations.joinToString(",")}") {
}