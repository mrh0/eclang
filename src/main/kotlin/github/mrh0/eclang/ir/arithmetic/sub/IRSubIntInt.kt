package github.mrh0.eclang.ir.arithmetic.sub

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.numbers.GsInt
import github.mrh0.eclang.context.Context

class IRSubIntInt(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsInt((left.evaluate(vm, c) as GsInt).value - (right.evaluate(vm, c) as GsInt).value)
    }

    override fun toString() = "($left i-i $right)"
}
