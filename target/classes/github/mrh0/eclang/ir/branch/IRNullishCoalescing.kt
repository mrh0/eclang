package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNullishCoalescing(location: Loc, val left: IIR, val right: IIR, val cast: IIR) : IR(location) {
    override fun toString() = "($left ?? $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        cast.toC(sb, c)
        sb.put(")__ec_nc(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(')')
    }
}