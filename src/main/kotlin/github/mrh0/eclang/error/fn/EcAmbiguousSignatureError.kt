package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.ect2.Type

/** More than one overload of [name] fits [argTypes] equally well. [candidates] lists them. */
class EcAmbiguousSignatureError(location: Loc, name: String, argTypes: List<Type>, candidates: List<String>) :
    EcError(location, "The call $name(${argTypes.joinToString(", ")}) fits several functions equally well: " +
        "${candidates.joinToString("; ")}. Convert an argument with `as` to pick one.") {
}
