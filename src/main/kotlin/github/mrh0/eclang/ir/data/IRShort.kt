package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRShort(location: Loc, val value: Short) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("$value")
    }

    override fun toString(): String = "s$value"
}