package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `return next;`, or `next; return;` when [next] has no value. */
class IRStatementReturn(location: Loc, val next: IIR?, val hasValue: Boolean = true) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (next != null && !hasValue) {
            sb.putStatement()
            next.toC(sb, c)
            sb.endStatement()
        }
        sb.putStatement(if (next != null && hasValue) "return " else "return")
        if (next != null && hasValue) next.toC(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "ret($next)"
    }
}
