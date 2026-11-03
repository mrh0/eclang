package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class IRParameter(location: Loc, val name: String, val type: EcType, private val def: IIR?) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (def != null) return
        if (type.isReferenceType()) sb.put("const ")
        IRType(location, type).toC(sb, c)
        sb.put(' ')
        sb.put(name)
    }

    fun getDefaultValue() = def
    fun hasDefaultValue() = def != null

    //fun get() = FunctionParameter(name, type, def)

    override fun toString(): String {
        return "IRParameter($name, $type, $def)"
    }
}