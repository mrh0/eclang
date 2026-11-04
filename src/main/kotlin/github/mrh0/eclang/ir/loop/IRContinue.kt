package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

/** `continue`, after running [deferred]: the statements deferred inside the loop body. */
class IRContinue(location: Loc, private val deferred: List<IIR> = listOf()) : IR(location) {
    override fun toString(): String {
        return "continue"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement("continue")
        sb.endStatement()
    }
}