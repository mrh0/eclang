package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementIf(location: Loc, private val conditions: List<IIR>, private val bodies: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "if($conditions, $bodies)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        conditions.forEachIndexed() { index,  it ->
            if (index == 0) {
                sb.putStatement("if (")
                it.toC(sb, c)
                sb.put(") ")
            }
            else {
                sb.put(" else if (")
                it.toC(sb, c)
                sb.put(") ")
            }
            sb.pushScope(BlockScope())
            bodies[index].toC(sb, c)
            sb.popScope()
        }
    }
}