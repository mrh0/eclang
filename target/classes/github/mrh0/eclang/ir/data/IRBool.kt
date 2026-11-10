package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRBool(location: Loc, val value: Boolean) : IRValue(location) {
    override fun toString(): String = "$value"

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (value) sb.put("true") else sb.put("false")
    }
}