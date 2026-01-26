package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcType

class EcNoMatchingCallSignatureError (location: Loc, name: String, argTypes: Array<EcType>) : EcError(location, "No matching function override for $name(${argTypes.joinToString(",")}) is defined") {
}