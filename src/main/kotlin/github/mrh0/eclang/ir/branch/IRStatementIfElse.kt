package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.BlockScope
import github.mrh0.eclang.output.c.CSourceBuilder

class IRStatementIfElse(location: Loc, private val conditions: List<IIR>, private val bodies: List<IIR>, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "if($conditions, $bodies, else: $elseBody)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        conditions.forEachIndexed() { index,  it ->
            if (index == 0) {
                sb.putStatement("if (")
                it.toC(sb, c)
                sb.put(") ")
            }
            else {
                sb.putStatement("else if (")
                it.toC(sb, c)
                sb.put(") ")
            }
            sb.pushScope(BlockScope())
            bodies[index].toC(sb, c)
            sb.popScope()
        }

        sb.putStatement("else ")
        sb.pushScope(BlockScope())
        elseBody.toC(sb, c)
        sb.popScope()
    }
}