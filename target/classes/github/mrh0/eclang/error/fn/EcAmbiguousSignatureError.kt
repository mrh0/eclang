package github.mrh0.eclang.error.fn

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.EcType

class EcAmbiguousSignatureError (location: Loc, namespace: String, name: String, argTypes: Array<EcType>, otherLocation: Loc) :
    EcError(location, "Ambiguous call signature for $name(${argTypes.joinToString(",")}) with $otherLocation in module '$namespace'")