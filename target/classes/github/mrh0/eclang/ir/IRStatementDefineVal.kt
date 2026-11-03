package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementDefineVal(location: Loc, private val ivar: IVar, private val expr: IIR) : IR(location) {
    override fun toString(): String {
        return "val($$ivar, $expr)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        sb.put("const ")
        IRType(location, ivar.getType()).toC(sb, c)
        sb.put(' ')
        sb.put(ivar.getName())
        sb.put(" = ")
        expr.toC(sb, c)
        sb.endStatement()
    }
}