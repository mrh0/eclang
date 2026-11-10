package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * The loop of `for x in iterator`: `for (;;)`, or `for (size_t i=0;;i++)` when [index] counts the rounds, so that `continue`
 * counts too. The [body] starts with the call of `next`, and leaves when it gives null.
 */
class IRStatementForeachNext(location: Loc, private val index: String?, private val body: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement(if (index == null) "for (;;) " else "for (size_t $index=0;;$index++) ")
        sb.pushScope(BlockScope())
        body.toC(sb, c)
        sb.popScope()
    }

    override fun toString() = "for($index, $body)"
}
