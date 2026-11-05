package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.output.c.CSourceBuilder

/** `expr.name`, or `expr->name` when [throughPointer]. */
class IRAccessorNamed(location: Loc, val expr: IIR, val name: String, val throughPointer: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        // `*q->x` would read the field of q first.
        val nested = expr is IRReadPointer
        if (nested) sb.put("(")
        expr.toC(sb, c)
        if (nested) sb.put(")")
        sb.put(if (throughPointer) "->$name" else ".$name")
    }

    override fun toString(): String {
        return "IRAccessorNamed($expr, $name)"
    }
}
