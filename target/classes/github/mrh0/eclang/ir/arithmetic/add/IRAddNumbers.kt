package github.mrh0.eclang.ir.arithmetic.add

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.numbers.GsFloat
import github.mrh0.eclang.values.numbers.GsNumber
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRAddNumbers(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsFloat((left.evaluate(vm, c) as GsNumber).getAsDouble() + (right.evaluate(vm, c) as GsNumber).getAsDouble())
    }

    override fun toString() = "($left i+i $right)"
}