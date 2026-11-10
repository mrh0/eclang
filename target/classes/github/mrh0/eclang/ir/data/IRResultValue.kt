package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Result

/** A `T throws E` result: `(R){.code = 0, .value = v}` for success, `(R){.code = 1, .error = e}` for an error. */
class IRResultValue(location: Loc, val type: Result, val isError: Boolean, val value: IIR?) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(type)}){.code = ${if (isError) 1 else 0}")
        if (value != null) {
            sb.put(if (isError) ", .error = " else ", .value = ")
            value.toC(sb, c)
        }
        sb.put("})")
    }

    override fun toString() = if (isError) "Error($value)" else "Ok($value)"
}
