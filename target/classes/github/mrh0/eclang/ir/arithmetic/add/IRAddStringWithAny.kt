package github.mrh0.eclang.ir.arithmetic.add

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsString
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRAddStringWithAny(location: Loc, val left: IIR, val right: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsString(left.evaluate(vm, c).toString() + right.evaluate(vm, c).toString())
    }

    override fun toString() = "($left s+? $right)"
}
