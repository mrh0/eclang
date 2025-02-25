package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.array.ArrayInstance
import github.mrh0.eclang.context.result.ResultInstance
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.internal.EcTypeVarArgC
import github.mrh0.eclang.types.numbers.*
import github.mrh0.eclang.types.numbers.signed.EcTypeChar
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort
import github.mrh0.eclang.types.numbers.unsigned.*

class IRType(location: Loc, val type: EcType) : IR(location) {
    override fun toString(): String {
        return type.toString()
    }

    private fun translateNative(t: EcType): String = when(t) {
        is EcTypeVarArgC -> "SHOULD NOT BE HERE"
        is EcTypeSize -> "size_t"
        is EcTypeInt -> "int"
        is EcTypeCString -> "char*"
        is EcTypeString -> "__ec_string_t"
        is EcTypeNone -> "void"
        is EcTypeAny -> "void"
        is EcTypeBool -> "bool"
        is EcTypeAtom, is EcTypeAtomInstance -> "char*"
        is EcTypeNullable -> translateNative(t.wrapped)
        is EcTypeRecord -> t.getSourceName()
        is EcTypeCStruct -> "struct ${t.getSourceName()}"
        is EcTypeResult -> ResultInstance.get(t.returnedType, t.throwableType).getId()
        is EcTypeChar -> "char"
        is EcTypeFloat -> "float"
        is EcTypeDouble -> "double"
        is EcTypeLong -> "long"
        is EcTypeShort -> "short"
        is EcTypeUChar -> "unsigned char"
        is EcTypeUInt -> "unsigned int"
        is EcTypeULong -> "unsigned long"
        is EcTypeUShort -> "unsigned short"
        is EcTypePointer -> "${translateNative(t.wrapped)}*"
        is EcTypeVolatile -> "volatile ${translateNative(t.wrapped)}"
        is EcTypeBoxed -> "${translateNative(t.wrapped)}*"
        is EcTypeGeneric -> throw EcError(location, "Generic type cannot be used in this context")
        is EcTypeCArray -> "${translateNative(t.arg)}*"
        is EcTypeArray -> ArrayInstance.get(t.arg).getId()
        is EcTypeUnion -> "union{${t.types.mapIndexed() { i, it -> "${translateNative(it)} v$i" }.joinToString(separator = ";", postfix = ";")}}"
        else -> throw NotImplementedError("Native type '$t' is not implemented")
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(translateNative(type))
    }
}