package github.mrh0.eclang.ir.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsTuple
import github.mrh0.eclang.context.Context

class IRTuple(location: Loc, val values: List<IIR>) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsTuple(values.map { it.evaluate(vm, c) }.toTypedArray())
    }

    override fun toString() = values.joinToString("&", "(", ")")
}
