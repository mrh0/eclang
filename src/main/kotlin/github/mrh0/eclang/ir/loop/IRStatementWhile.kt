package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementWhile(location: Loc, private val condition: IIR, private val body: IIR) : IR(location) {
    override fun toString(): String {
        return "while($condition, $body)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("while (")
        condition.toC(sb, c)
        sb.put(") ")
        sb.pushScope(BlockScope())
        body.toC(sb, c)
        sb.popScope()
    }
}