package github.mrh0.eclang.ir.accessor

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRConvert
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.Type

/**
 * A C array holding a [type] (a `T[N]`): a record field, or a C global. It is read as the struct eclang uses for a `T[N]`,
 * which has the same bytes, so it can be copied as a whole and assigned. With [packed] it may be at any address.
 */
class IRFixedStorage(location: Loc, val array: IIR, private val type: Type, private val packed: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.storage(sb.render { array.toC(it, c) }, type, packed))
    }

    override fun toString() = "storage($array)"
}

/** The elements of [expr], a [type], as C sees them. Those of a [IRFixedStorage] are the C array, unless they are arrays too, which C sees differently. */
private fun elements(expr: IIR, type: FixedArray, sb: CSourceBuilder, c: Context) {
    if (expr is IRFixedStorage && type.element !is FixedArray) return expr.array.toC(sb, c)
    // `.` binds tighter than the `*` of `@p`.
    val value = sb.render { expr.toC(it, c) }
    sb.put(if (value.startsWith("*")) "($value).data" else "$value.data")
}

/** `a.data` on [type], a `T[N]`, or through a pointer to one. */
class IRFixedData(location: Loc, private val expr: IIR, private val type: FixedArray, private val throughPointer: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (!throughPointer) return elements(expr, type, sb, c)
        expr.toC(sb, c)
        sb.put("->data")
    }

    override fun toString() = "$expr.data"
}

/** `a[i]` on [type], a `T[N]`. When [checked], `__ec_index` stops the program if i is out of bounds. */
class IRAccessorFixed(location: Loc, private val expr: IIR, private val index: IIR, private val type: FixedArray, private val checked: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        elements(expr, type, sb, c)
        sb.put('[')
        if (checked) sb.put("__ec_index(")
        index.toC(sb, c)
        if (checked) sb.put(", ${type.size}, ${IRConvert.where(location)})")
        sb.put(']')
    }

    override fun toString() = "($expr[$index])"
}

/** [expr], a [type], as the array [array] pointing to its elements: `(A){4, x.data}`. [expr] must stay around, so it can't be a temporary. */
class IRFixedView(location: Loc, private val expr: IIR, private val array: ArrayType, private val type: FixedArray) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${sb.types.cName(array)}){${type.size}, ")
        elements(expr, type, sb, c)
        sb.put("})")
    }

    override fun toString() = "view($expr)"
}
