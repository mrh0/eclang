package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementReturnDefer(location: Loc, val next: IIR, val nextType: IIR, val deferred: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "ret($next)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement()
        nextType.toC(sb, c)
        sb.put(" __ec_ret = ")
        next.toC(sb, c)
        sb.endStatement()
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement("return __ec_ret")
        sb.endStatement()
    }
}