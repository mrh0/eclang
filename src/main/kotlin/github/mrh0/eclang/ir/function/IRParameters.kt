package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRParameters(location: Loc, val args: List<IRParameter>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (args.size == 0) {
            sb.put("void")
            return
        }
        args.forEachIndexed { index, iir ->
            iir.toC(sb, c)
            if (index < args.size-1) sb.put(", ")
        }
    }

    override fun toString(): String {
        return "$args"
    }
}