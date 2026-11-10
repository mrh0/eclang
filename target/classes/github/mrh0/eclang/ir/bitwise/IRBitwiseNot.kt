package github.mrh0.eclang.ir.bitwise

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `~x`. [castTo]: cut the result back to this (small) type, because C flips the bits of an int. */
class IRBitwiseNot(location: Loc, private val expr: IIR, private val castTo: Type?) : IR(location) {
    override fun isConstant() = expr.isConstant()

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(if (castTo != null) "((${sb.types.cName(castTo)})~(" else "(~(")
        expr.toC(sb, c)
        sb.put("))")
    }

    override fun toString() = "~$expr"
}
