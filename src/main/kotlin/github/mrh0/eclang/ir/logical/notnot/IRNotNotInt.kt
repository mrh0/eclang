package github.mrh0.eclang.ir.logical.notnot

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.context.Context

class IRNotNotInt(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "(!!i $expr)"

    override fun deterministic() = expr.deterministic()
}
