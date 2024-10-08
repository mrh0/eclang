package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRFloat(location: Loc, val value: Float) : IRValue(location) {
    override fun toString(): String = "f$value"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${value}f")
    }
}