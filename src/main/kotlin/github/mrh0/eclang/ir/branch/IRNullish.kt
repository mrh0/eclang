package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * `left ?? right` on a nullable value: `(left is not null ? left as T : right)`. [left] must be free of side effects
 * (a variable or a temporary), because it is read twice.
 */
class IRNullish(location: Loc, val left: IIR, val leftType: Type, val right: IIR, val resultType: Type) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val l = sb.render { left.toC(it, c) }
        val present = sb.types.isCheck(leftType, Types.Null, l)
        val value = sb.types.narrow(leftType, resultType, l, IRConvert.where(location))
        sb.put("(!$present ? $value : ")
        right.toC(sb, c)
        sb.put(")")
    }

    override fun toString() = "Nullish($left ?? $right)"
}
