package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.output.c.CSourceBuilder

/** `T name = expr;` (`val`; eclang itself stops reassignment, so `addrof` gives a plain `T*`). */
class IRStatementDefineVal(location: Loc, private val ivar: IVar, private val expr: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${sb.types.cName(ivar.getType())} ${ivar.cName()} = ")
        expr.toC(sb, c)
        sb.endStatement()
    }

    override fun toString(): String {
        return "IRStatementDefineVal(${ivar.getName()}, $expr)"
    }
}
