package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `(first, value)`: evaluates [first] for its effect, then gives [value]. */
class IRComma(location: Loc, val first: IIR, val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("(")
        first.toC(sb, c)
        sb.put(", ")
        value.toC(sb, c)
        sb.put(")")
    }

    override fun toString() = "($first, $value)"
}
