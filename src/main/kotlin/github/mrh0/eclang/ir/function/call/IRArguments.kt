package github.mrh0.eclang.ir.function.call

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRArguments(location: Loc, val args: List<IRArgument>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        args.forEachIndexed { index, iir ->
            iir.toC(sb, c)
            if (index < args.size-1) sb.put(", ")
        }
    }

    override fun toString(): String = "$args"
}