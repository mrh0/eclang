package github.mrh0.eclang.types

import github.mrh0.eclang.types.numbers.EcTypeFloat
import github.mrh0.eclang.types.numbers.EcTypeInt

object BuiltInTypes {
    private val builtInPrimitiveIdentityTypes: List<EcType> = listOf(
        EcTypeInt,
        EcTypeFloat,
        EcTypeBool,
        EcTypeString,
        EcTypeNone,
        EcTypeAtom,
        EcTypeNull
    )
    val builtInNamespaceMap: Map<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.toString() }
    val builtInNameMap: Map<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.identifier }
    fun getType(test: String): EcType = builtInNameMap.getOrElse(test) { builtInNamespaceMap.getOrElse(test) { throw Exception("No such type $test") } }
}
