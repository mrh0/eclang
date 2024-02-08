package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.function.IRFunc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRProgram(location: Loc, val functions: List<IRFunc>) : IR(location) {
    override fun toString(): String {
        return "$functions"
    }

    fun getMainFunction() = functions.find { it.name == "main" } ?: throw EcError(location, "No 'main' function defined")

    override fun evaluate(vm: VM, c: Context): GsBase {
        return getMainFunction().evaluate(vm, c)
    }
}