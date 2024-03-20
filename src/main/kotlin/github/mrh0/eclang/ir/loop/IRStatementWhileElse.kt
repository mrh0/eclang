package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRStatementWhileElse(location: Loc, private val condition: IIR, private val body: IIR, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "whileElse($condition, $body)"
    }
}