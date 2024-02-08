package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRInlineIf(location: Loc, private val condition: IIR, private val body: IIR, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "if($condition, $body, $elseBody)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        return if((condition.evaluate(vm, c) as GsBool).value) body.evaluate(vm, c) else elseBody.evaluate(vm, c)
    }
}