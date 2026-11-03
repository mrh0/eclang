package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRContinue(location: Loc) : IR(location) {
    override fun toString(): String {
        return "continue"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("continue")
        sb.endStatement()
    }
}