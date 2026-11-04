package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type
import github.mrh0.ect2.Union

/**
 * `expr?.field`: a call of a generated helper that returns null when [expr] (of nullable type [from]) is null, and
 * the field (of [fieldType], as the nullable [result]) otherwise. The helper evaluates [expr] only once.
 */
class IRSafeAccess(
    location: Loc, private val expr: IIR, private val from: Union, private val nonNull: Type,
    private val field: String, private val fieldType: Type, private val result: Type, private val throughPointer: Boolean,
) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.safeAccess(from, nonNull, field, fieldType, result, throughPointer) + "(")
        expr.toC(sb, c)
        sb.put(")")
    }

    override fun toString() = "$expr?.$field"
}
