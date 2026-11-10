package github.mrh0.eclang.ir.compare.equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Record
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Type

/** `a == b` (or `!=` when [negated]) on two records or tuples of [type]: a generated helper compares them field by field. */
class IREqualsRecord(location: Loc, private val left: IIR, private val right: IIR, private val type: Type, private val negated: Boolean) : IR(location) {
    override fun toString() = "($left ${if (negated) "!=" else "=="} $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        val helper = when (type) {
            is Tuple -> sb.types.tupleEquals(type)
            is FixedArray -> sb.types.fixedEquals(type)
            else -> sb.types.recordEquals(type as Record)
        }
        sb.put("${if (negated) "!" else ""}$helper(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(")")
    }
}
