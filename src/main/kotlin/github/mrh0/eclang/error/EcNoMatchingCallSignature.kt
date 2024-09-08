package github.mrh0.eclang.error

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.types.EcType

class EcNoMatchingCallSignature (location: Loc, name: String, argTypes: Array<EcType>) : EcError(location, "No matching function override for $name(${argTypes.joinToString(",")}) is defined") {
}