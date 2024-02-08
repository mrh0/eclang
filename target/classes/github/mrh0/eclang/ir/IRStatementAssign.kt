package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRStatementAssign(location: Loc, private val index: Int, private val expr: IIR) : IR(location) {
    override fun toString(): String {
        return "assign($$index, $expr)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        c.setValue(location, index, expr.evaluate(vm, c))
        return GsValueNone
    }
}