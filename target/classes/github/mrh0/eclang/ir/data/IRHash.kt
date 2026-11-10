package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `hash(value)` of a [type] the program has no `hash` function for (see CTypes.hash). */
class IRHash(location: Loc, private val type: Type, private val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.hash(type, sb.render { value.toC(it, c) }))
    }

    override fun toString() = "hash($value)"
}
