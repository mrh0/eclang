package github.mrh0.eclang.ir.logical.not

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNotInt(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "(!i $expr)"

    override fun deterministic() = expr.deterministic()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        expr.toC(sb, c)
        sb.put("<1)")
    }
}
