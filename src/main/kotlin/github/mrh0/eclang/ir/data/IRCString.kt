package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.StringLiteral

class IRCString(location: Loc, val value: StringLiteral) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('"')
        sb.put(value.toC())
        sb.put('"')
    }

    override fun toString(): String = "'$value'"
}