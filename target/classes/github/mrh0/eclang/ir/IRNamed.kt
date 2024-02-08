package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRNamed(location: Loc, private val name: String, private val index: Int) : IR(location) {
    override fun toString(): String {
        return "$$index"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        return c.getValue(location, index)
    }
}