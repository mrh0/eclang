package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRInlineIf(location: Loc, private val condition: IIR, private val body: IIR, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "if($condition, $body, $elseBody)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put('(')
        condition.toC(sb, c)
        sb.put(") ? (")
        condition.toC(sb, c)
        sb.put(") : (")
        condition.toC(sb, c)
        sb.put(')')
    }
}