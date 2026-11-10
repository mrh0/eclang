package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `a[i]` on an array or String ([arrayType]). When [checked], a helper stops the program if i is out of bounds;
 * it returns a pointer, so the access is still a C lvalue.
 */
class IRAccessorArray(location: Loc, val expr: IIR, val exprIndex: IIR, private val arrayType: Type, private val checked: Boolean) : IR(location) {
    override fun toString() = "($expr[$exprIndex])"

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (!checked) {
            expr.toC(sb, c)
            sb.put(".data[")
            exprIndex.toC(sb, c)
            sb.put(']')
            return
        }
        sb.put("(*")
        pointerC(sb, c)
        sb.put(")")
    }

    /** The checked element's address, `__ec_at_AInt(a, i, "where")`: what `&` of it is. */
    fun pointerC(sb: CSourceBuilder, c: Context) {
        sb.put("${sb.types.elementAt(arrayType)}(")
        expr.toC(sb, c)
        sb.put(", ")
        exprIndex.toC(sb, c)
        sb.put(", ${IRConvert.where(location)})")
    }

    /** Whether the element is read through the bounds check, a call that gives its address (see [pointerC]). */
    val isChecked get() = checked
}
