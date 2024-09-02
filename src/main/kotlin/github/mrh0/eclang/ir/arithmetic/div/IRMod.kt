package github.mrh0.eclang.ir.arithmetic.div

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRMod(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun toString() = "($left % $right)"

    override fun deterministic() = left.deterministic() && right.deterministic()

    override fun toC(sb: CSourceBuilder, c: Context) {
        left.toC(sb, c)
        sb.put('%')
        right.toC(sb, c)
    }
}
