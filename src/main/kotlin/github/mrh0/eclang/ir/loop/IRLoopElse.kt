package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * A loop with an `else` block, which runs when the loop ends without `break`. In C the block simply follows the
 * loop, and a `break` inside the loop jumps past it, to [label] (null when there is no such `break`).
 */
class IRLoopElse(location: Loc, private val loop: IIR, private val elseBody: IIR, private val label: String?) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        loop.toC(sb, c)
        sb.putStatement()
        sb.pushScope(BlockScope())
        elseBody.toC(sb, c)
        sb.popScope()
        if (label != null) sb.putStatement("$label:;\n")
    }

    override fun toString() = "$loop else $elseBody"
}
