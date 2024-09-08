package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.*
import github.mrh0.eclang.types.numbers.EcTypeInt

class IRType(location: Loc, val type: EcType) : IR(location) {
    fun translateNative(t: EcType): String = when(t) {
        is EcTypeInt -> "int"
        is EcTypeCString -> "char*"
        is EcTypeNone -> "void"
        is EcTypeAtom, is EcTypeAtomInstance -> "char*"
        is EcTypeNullable -> translateNative(t.wrapped)
        else -> throw NotImplementedError("Native type '$t' is not implemented.")
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(translateNative(type))
    }
}