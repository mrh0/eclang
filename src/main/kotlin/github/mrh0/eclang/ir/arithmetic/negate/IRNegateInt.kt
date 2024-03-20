package github.mrh0.eclang.ir.arithmetic.negate

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRNegateInt(location: Loc, val expr: IIR) : IR(location) {
    override fun toString() = "(-i $expr)"

    override fun deterministic() = expr.deterministic()
}
