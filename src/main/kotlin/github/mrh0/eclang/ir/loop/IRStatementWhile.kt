package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRStatementWhile(location: Loc, private val condition: IIR, private val body: IIR) : IR(location) {
    override fun toString(): String {
        return "while($condition, $body)"
    }
}