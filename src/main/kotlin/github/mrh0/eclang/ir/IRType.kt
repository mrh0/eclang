package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.numbers.*
import github.mrh0.eclang.types.numbers.signed.EcTypeChar
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort
import github.mrh0.eclang.types.numbers.unsigned.EcTypeByte

class IRType(location: Loc, val type: EcType) : IR(location) {
    override fun toString(): String {
        return type.toString()
    }

    private fun translateNative(t: EcType): String = when(t) {
        is EcTypeInt -> "int"
        is EcTypeCString -> "char*"
        is EcTypeNone -> "void"
        is EcTypeAny -> "void"
        is EcTypeBool -> "bool"
        is EcTypeAtom, is EcTypeAtomInstance -> "char*"
        is EcTypeNullable -> translateNative(t.wrapped)
        is EcTypeRecord -> "struct ${t.getSourceName()}"
        is EcTypeDefRecord -> t.getSourceName()
        is EcTypeChar -> "char"
        is EcTypeFloat -> "float"
        is EcTypeDouble -> "double"
        is EcTypeLong -> "long"
        is EcTypeShort -> "short"
        is EcTypeByte -> "unsigned char"
        is EcTypePointer -> "${translateNative(t.wrapped)}*"
        is EcTypeBoxed -> "${translateNative(t.wrapped)}*"
        is EcTypeGeneric -> throw EcError(location, "Generic type cannot be used in this context")
        is EcTypeArray -> "${t.arg}[]"
        else -> throw NotImplementedError("Native type '$t' is not implemented")
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(translateNative(type))
    }
}