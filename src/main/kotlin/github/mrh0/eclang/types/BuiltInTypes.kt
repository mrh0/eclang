package github.mrh0.eclang.types

import github.mrh0.eclang.types.numbers.EcTypeFloat
import github.mrh0.eclang.types.numbers.EcTypeInt

object BuiltInTypes {
    private val builtInPrimitiveIdentityTypes: List<EcType> = listOf(
        EcTypeInt,
        EcTypeFloat,
        EcTypeBool,
        EcTypeCString,
        EcTypeNone,
        EcTypeAtom,
        EcTypeNull
    )
    val builtInNamespaceMap: MutableMap<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.toString() }.toMutableMap()
    val builtInNameMap: MutableMap<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.identifier }.toMutableMap()
    fun defineType(namespace: String, identifier: String, type: EcType) {
        builtInNamespaceMap["$namespace.$identifier"] = type
        builtInNameMap[identifier] = type
    }
    fun getType(test: String): EcType = builtInNamespaceMap.getOrElse(test) { builtInNameMap.getOrElse(test) { throw Exception("No such type $test") } }
}
