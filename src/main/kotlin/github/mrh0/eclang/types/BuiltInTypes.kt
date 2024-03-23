package github.mrh0.eclang.types

import github.mrh0.eclang.types.numbers.EcTypeFloat
import github.mrh0.eclang.types.numbers.EcTypeInt

object BuiltInTypes {
    private val builtInPrimitives: List<EcType> = listOf(
        EcTypeInt,
        EcTypeFloat,
        EcTypeBool,
        EcTypeString,
        EcTypeAtom,
        EcTypeNone
    )
    val builtInNamespaceMap: Map<String, EcType> = builtInPrimitives.associateBy { it.toString() }
    val builtInNameMap: Map<String, EcType> = builtInPrimitives.associateBy { it.identifier }
    fun getType(test: String): EcType = builtInNameMap.getOrElse(test) { builtInNamespaceMap.getOrElse(test) { throw Exception("No such type $test") } }
}

