package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `clone(value)`: a deep copy of [value] (a [type]) in [pool], made by a generated `__ec_clone_T` helper (see CTypes.clone). */
class IRClone(location: Loc, private val type: Type, private val value: IIR, private val pool: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.clone(type, sb.render { value.toC(it, c) }, sb.render { pool.toC(it, c) }))
    }

    override fun toString() = "clone($value, $pool)"
}
