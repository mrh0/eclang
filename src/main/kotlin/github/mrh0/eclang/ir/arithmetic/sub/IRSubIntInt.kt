package github.mrh0.eclang.ir.arithmetic.sub

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRSubIntInt(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun toString() = "($left i-i $right)"
}
