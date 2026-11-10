package github.mrh0.eclang.ir.compare.greater

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRGreaterOrEqualsStringString(location: Loc, private val left: IIR, private val right: IIR) : IR(location) {
    override fun toString() = "($left s>=s $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("strcmp(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(") >= 0")
    }
}
