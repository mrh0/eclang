package github.mrh0.eclang.ir.compare.equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Record

/** `a == b` (or `!=` when [negated]) on two [record] values: a generated helper compares them field by field. */
class IREqualsRecord(location: Loc, private val left: IIR, private val right: IIR, private val record: Record, private val negated: Boolean) : IR(location) {
    override fun toString() = "($left ${if (negated) "!=" else "=="} $right)"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${if (negated) "!" else ""}${sb.types.recordEquals(record)}(")
        left.toC(sb, c)
        sb.put(", ")
        right.toC(sb, c)
        sb.put(")")
    }
}
