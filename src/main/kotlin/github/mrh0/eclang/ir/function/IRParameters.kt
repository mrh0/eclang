package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.types.EcTypeNone

class IRParameters(location: Loc, val args: List<IRParameter>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (args.isEmpty()) {
            sb.put("void")
            return
        }
        args.forEachIndexed { index, iir ->
            if (index != 0 && iir.def == null) sb.put(", ")
            iir.toC(sb, c)
        }
    }

    override fun toString() = "$args"
}