package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRExprNest(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "($expr)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        expr.toC(sb, c)
        sb.put(')')
    }
}
