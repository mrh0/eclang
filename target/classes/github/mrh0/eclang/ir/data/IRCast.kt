package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRCast(location: Loc, val type: IIR, val expr: IIR) : IR(location) {
    override fun toString() = "Cast($type, $expr)"

    override fun deterministic() = expr.deterministic()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        type.toC(sb, c)
        sb.put(')')
        expr.toC(sb, c)
    }
}
