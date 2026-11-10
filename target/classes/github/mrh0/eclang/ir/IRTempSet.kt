package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `(name = value)`: sets a temporary inside an expression, so the value is evaluated where it is written, and only then. */
class IRTempSet(location: Loc, val name: String, val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("($name = ")
        value.toC(sb, c)
        sb.put(")")
    }

    override fun toString() = "($name = $value)"
}
