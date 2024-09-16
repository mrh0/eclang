package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRAccessorNamed(location: Loc, val expr: IIR, val name: String) : IR(location) {
    override fun toString() = "($expr)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        expr.toC(sb, c)
        sb.put(".$name")
    }
}