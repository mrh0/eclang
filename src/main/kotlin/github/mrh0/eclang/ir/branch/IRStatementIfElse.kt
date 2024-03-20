package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRStatementIfElse(location: Loc, private val conditions: List<IIR>, private val bodies: List<IIR>, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "if($conditions, $bodies, else: $elseBody)"
    }
}