package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRStatementIf(location: Loc, private val conditions: List<IIR>, private val bodies: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "if($conditions, $bodies)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        for (i in conditions.indices) {
            if((conditions[i].evaluate(vm, c) as GsBool).value) {
                return bodies[i].evaluate(vm, c)
            }
        }
        return GsValueNone
    }
}