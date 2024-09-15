package github.mrh0.eclang.ir.compare.not_equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNotEquals(location: Loc, private val left: IIR, private val right: IIR) : IR(location) {
    override fun toString() = "($left i!=i $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        left.toC(sb, c)
        sb.put("!=")
        right.toC(sb, c)
    }
}
