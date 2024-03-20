package github.mrh0.eclang.ir.logical.not

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRNotBool(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "(!b $expr)"

    override fun deterministic() = expr.deterministic()
}
