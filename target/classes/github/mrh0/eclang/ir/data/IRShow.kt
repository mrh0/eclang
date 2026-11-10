package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** [value], a [type] with no printf conversion, as a String for `${...}` (see CTypes.show). It is copied from the current pool into the result. */
class IRShow(location: Loc, private val type: Type, private val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.show(type, sb.render { value.toC(it, c) }, TNamed.CURRENT_POOL))
    }

    override fun toString() = "show($value)"
}
