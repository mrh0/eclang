package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** A block's statements, then its deferred statements. [diverges]: the block always leaves early (ret, throw, ...). */
class IRBlock(location: Loc, val statements: List<IIR>, val deferred: List<IIR>, val diverges: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        statements.forEach { it.toC(sb, c) }
        deferred.forEach { it.toC(sb, c) }
    }

    override fun toString(): String {
        return "IRBlock($statements)"
    }
}
