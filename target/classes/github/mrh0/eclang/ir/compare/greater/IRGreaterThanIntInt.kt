package github.mrh0.eclang.ir.compare.greater

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.values.numbers.GsInt
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRGreaterThanIntInt(location: Loc, private val left: IIR, private val right: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsBool((left.evaluate(vm, c) as GsInt).value > (right.evaluate(vm, c) as GsInt).value)
    }

    override fun toString() = "($left i>i $right)"
}