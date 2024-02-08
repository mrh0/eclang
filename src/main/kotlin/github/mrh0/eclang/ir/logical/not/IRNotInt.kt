package github.mrh0.eclang.ir.logical.not

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.values.numbers.GsInt
import github.mrh0.eclang.context.Context

class IRNotInt(location: Loc, val expr: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsBool((expr.evaluate(vm, c) as GsInt).value > 0)
    }

    override fun toString() = "(!i $expr)"

    override fun deterministic() = expr.deterministic()
}
