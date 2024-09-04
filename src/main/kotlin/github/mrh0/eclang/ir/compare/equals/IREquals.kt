package github.mrh0.eclang.ir.compare.equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IREquals(location: Loc, private val left: IIR, private val right: IIR) : IR(location) {
    override fun toString() = "($left i==i $right)"
}