package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.output.c.CSourceBuilder

/** `T name = expr;` (`var`). */
class IRStatementDefineVar(location: Loc, private val ivar: IVar, private val expr: IIR, private val volatile: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${if (volatile) "volatile " else ""}${sb.types.cName(ivar.getType())} ${ivar.cName()} = ")
        expr.toC(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "IRStatementDefineVar(${ivar.getName()}, $expr)"
    }
}
