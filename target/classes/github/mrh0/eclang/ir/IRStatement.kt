package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatement(location: Loc, val next: IIR) : IR(location) {
    override fun toString(): String {
        return "$next"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        next.toC(sb, c)
        sb.endStatement()
    }
}