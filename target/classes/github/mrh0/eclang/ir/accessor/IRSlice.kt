package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** A slice with both ends, or from one index up to the end of the value when [to] is null: a view of part of an array or String ([arrayType]), sharing its memory. */
class IRSlice(
    location: Loc, private val expr: IIR, private val from: IIR, private val to: IIR?, private val inclusive: Boolean,
    private val arrayType: Type, private val checked: Boolean,
) : IR(location) {
    override fun toString() = "($expr[$from..${if (inclusive) "=" else "<"}$to])"

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (to == null) {
            sb.put("${sb.types.sliceFrom(arrayType, checked)}(")
            expr.toC(sb, c)
            sb.put(", ")
            from.toC(sb, c)
            sb.put(", ${IRConvert.where(location)})")
            return
        }
        sb.put("${sb.types.slice(arrayType, checked)}(")
        expr.toC(sb, c)
        sb.put(", ")
        from.toC(sb, c)
        sb.put(", ")
        if (inclusive) sb.put('(')
        to.toC(sb, c)
        if (inclusive) sb.put(") + 1")
        sb.put(", ${IRConvert.where(location)})")
    }
}
