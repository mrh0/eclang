package github.mrh0.eclang.ir.logical.or

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.logical.and.IRAnd
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IROr(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun toString() = "($left || $right)"

    override fun isConstant() = left.isConstant() && right.isConstant()

    override fun toC(sb: CSourceBuilder, c: Context) {
        side(sb, c, left)
        sb.put("||")
        side(sb, c, right)
    }

    /** An `and` inside an `or` binds the same in C, but gcc asks for parentheses (-Wparentheses), so it gets them. */
    private fun side(sb: CSourceBuilder, c: Context, ir: IIR) {
        if (ir !is IRAnd) return ir.toC(sb, c)
        sb.put('(')
        ir.toC(sb, c)
        sb.put(')')
    }
}
