package github.mrh0.eclang.ir.bitwise

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `(left op right)` for `&`, `|`, `^`, `<<`, `>>`. Always in parentheses: in eclang `a & b == c` is `(a & b) == c`,
 * but C would read it as `a & (b == c)`. [castTo]: cut the result back to this (small) type.
 */
class IRBitwise(location: Loc, private val op: String, private val left: IIR, private val right: IIR, private val castTo: Type?) : IR(location) {
    override fun isConstant() = left.isConstant() && right.isConstant()

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (castTo != null) sb.put("((${sb.types.cName(castTo)})")
        sb.put('(')
        left.toC(sb, c)
        sb.put(" $op ")
        right.toC(sb, c)
        sb.put(')')
        if (castTo != null) sb.put(')')
    }

    override fun toString() = "($left $op $right)"
}
