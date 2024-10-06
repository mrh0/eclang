package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementForeachInRange(location: Loc, private val varName: String, private val varType: IRType, private val lower: IIR, private val upper: IIR, private val body: IIR) : IR(location) {
    override fun toString(): String {
        return "for($varName, $lower, $upper, $body)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("for (")
        varType.toC(sb, c)
        sb.put(" $varName=")
        lower.toC(sb, c)
        sb.put(";$varName<")
        upper.toC(sb, c)
        sb.put(";$varName++")
        sb.put(") ")
        sb.pushScope(BlockScope())
        body.toC(sb, c)
        sb.popScope()
    }
}