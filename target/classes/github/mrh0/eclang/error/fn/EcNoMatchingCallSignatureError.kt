package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.ect2.Type

/** No overload of [name] accepts [argTypes]. [details] explains the closest candidates. */
class EcNoMatchingCallSignatureError(location: Loc, name: String, argTypes: List<Type>, details: String) :
    EcError(location, "No function $name(${argTypes.joinToString(", ")}) is defined. $details") {
}
