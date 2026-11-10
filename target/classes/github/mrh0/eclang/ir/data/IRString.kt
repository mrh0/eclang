package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.eclang.util.StringLiteral

class IRString(location: Loc, val value: StringLiteral) : IRValue(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("(__ec_string_t){${value.size},\"")
        sb.put(value.toC())
        sb.put("\"}")
    }

    override fun toString(): String = "'$value'"
}