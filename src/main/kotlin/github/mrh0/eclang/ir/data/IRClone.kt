package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `clone(value)`: a deep copy of [value] (a [type]) in [pool], made by a generated `__ec_clone_T` helper (see CTypes.clone);
 * with [shallow], `copy(value)`: a copy one level deep (see CTypes.shallowCopy).
 */
class IRClone(location: Loc, private val type: Type, private val value: IIR, private val pool: IIR, private val shallow: Boolean = false) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        val (v, p) = sb.render { value.toC(it, c) } to sb.render { pool.toC(it, c) }
        sb.put(if (shallow) sb.types.shallowCopy(type, v, p) else sb.types.clone(type, v, p))
    }

    override fun toString() = "${if (shallow) "copy" else "clone"}($value, $pool)"
}
