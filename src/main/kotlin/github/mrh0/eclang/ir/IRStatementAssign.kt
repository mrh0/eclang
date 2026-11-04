package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `target = expr`, or `target op= expr` when [op] isn't empty. [target] is a C lvalue. */
class IRStatementAssign(location: Loc, private val target: IIR, private val op: String, private val expr: IIR) : IR(location) {
    override fun toString(): String {
        return "assign($target $op= $expr)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        target.toC(sb, c)
        sb.put(" $op= ")
        expr.toC(sb, c)
        sb.endStatement()
    }
}
