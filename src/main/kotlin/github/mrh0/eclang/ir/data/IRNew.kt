package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.ArrayType
import github.mrh0.ect2.Type

/**
 * `new` in [pool]: with a [value], a pointer to a copy of it (`__ec_new_Point(pool, v)`); with a [count], an array
 * [type] of that many empty elements (`__ec_alloc_AInt(pool, n)`); with neither, a pointer to an empty [type].
 */
class IRNew(location: Loc, private val type: Type, private val pool: IIR, private val value: IIR?, private val count: IIR? = null) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(if (count != null) sb.types.newArray(type as ArrayType) else sb.types.newValue(type))
        sb.put("(")
        pool.toC(sb, c)
        sb.put(", ")
        when {
            count != null -> count.toC(sb, c)
            value != null -> value.toC(sb, c)
            else -> sb.put(sb.types.zero(type))
        }
        sb.put(")")
    }

    override fun toString() = "new($type, $pool, ${value ?: count})"
}
