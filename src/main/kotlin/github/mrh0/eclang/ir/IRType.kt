package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** The C spelling of [type] (see CTypes for the representation rules). */
class IRType(location: Loc, val type: Type) : IR(location) {
    override fun toString(): String {
        return type.toString()
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(sb.types.cName(type))
    }
}
