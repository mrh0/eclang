package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRTailIf(location: Loc, private val body: IIR, private val condition: IIR) : IR(location) {
    override fun toString(): String {
        return "tailIf($condition, $body)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("if (")
        condition.toC(sb, c)
        sb.put(") ")
        sb.pushScope(BlockScope())
        body.toC(sb, c)
        sb.popScope()
    }
}