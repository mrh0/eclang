package github.mrh0.eclang.types

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.types.numbers.*
import github.mrh0.eclang.types.numbers.signed.EcTypeChar
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort
import github.mrh0.eclang.types.numbers.unsigned.EcTypeByte

object BuiltInTypes {
    private val builtInPrimitiveIdentityTypes: List<EcType> = listOf(
        EcTypeInt,
        EcTypeFloat,
        EcTypeDouble,
        EcTypeLong,
        EcTypeShort,
        EcTypeBool,
        EcTypeCString,
        EcTypeNone,
        EcTypeAtom,
        EcTypeNull,
        EcTypeChar,
        EcTypeByte,
        EcTypeAny
    )
    private val builtInNamespaceMap: MutableMap<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.toString() }.toMutableMap()
    private val builtInNameMap: MutableMap<String, EcType> = builtInPrimitiveIdentityTypes.associateBy { it.identifier }.toMutableMap()
    fun defineType(location: Loc, namespace: String, identifier: String, type: EcType) {
        if (builtInNamespaceMap["$namespace.$identifier"] != null || builtInNameMap[identifier] != null) throw EcError(location, "Type '$identifier' is already defined.)")
        builtInNamespaceMap["$namespace.$identifier"] = type
        builtInNameMap[identifier] = type
    }
    fun getType(test: String): EcType = builtInNamespaceMap.getOrElse(test) { builtInNameMap.getOrElse(test) { throw Exception("No such type $test") } }
}
