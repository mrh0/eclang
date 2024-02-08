package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsFunction
import github.mrh0.eclang.context.Context

class IRFunctionCall(location: Loc, val name: String, val contextIndex: Int, val args: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "$name($args)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        val fn = c.getValue(location, contextIndex) as GsFunction
        return fn.call(location, vm, vm.getContextOrDefault(name, c), args.map { it.evaluate(vm, c) }.toTypedArray())
    }
}