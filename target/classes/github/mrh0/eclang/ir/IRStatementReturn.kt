package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementReturn(location: Loc, val next: IIR) : IR(location) {
    override fun toString(): String {
        return "ret($next)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("return ")
        next.toC(sb, c)
        sb.endStatement()
    }
}