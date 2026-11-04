package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * `break`, or `goto label` out of a loop that has an `else` block (see [IRLoopElse]). [deferred]: the statements
 * deferred inside the loop body, which run first.
 */
class IRBreak(location: Loc, private val label: String? = null, private val deferred: List<IIR> = listOf()) : IR(location) {
    override fun toString(): String {
        return "break"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement(if (label == null) "break" else "goto $label")
        sb.endStatement()
    }
}
