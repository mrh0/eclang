package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRChar(location: Loc, val value: String) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('\'')
        sb.put(value)
        sb.put('\'')
    }

    override fun toString(): String = "'$value'"
}