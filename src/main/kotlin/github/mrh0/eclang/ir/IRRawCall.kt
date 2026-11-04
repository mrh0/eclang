package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** A call of a runtime helper, e.g. `__ec_str_eq(a, b)`. */
class IRRawCall(location: Loc, val name: String, val args: List<IIR>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("$name(")
        args.forEachIndexed { i, it -> if (i != 0) sb.put(", "); it.toC(sb, c) }
        sb.put(")")
    }

    override fun toString() = "$name($args)"
}
