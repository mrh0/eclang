package github.mrh0.eclang.ir.arithmetic.pow

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `a ** b` on whole numbers of [type]: a generated helper that multiplies in [type] (no `double`, no libm). */
class IRPowInt(location: Loc, val left: IIR, val right: IIR, private val type: Type) : IR(location) {
    override fun toString() = "($left ** $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.intPow(type)}(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(')')
    }
}
