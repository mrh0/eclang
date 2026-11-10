package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * `continue`, or `goto label` to the end of the body of an outer loop (`continue outer`), after running [deferred]: the
 * statements deferred inside the loop body.
 */
class IRContinue(location: Loc, private val deferred: List<IIR> = listOf(), private val label: String? = null) : IR(location) {
    override fun toString(): String {
        return "continue"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement(if (label == null) "continue" else "goto $label")
        sb.endStatement()
    }
}