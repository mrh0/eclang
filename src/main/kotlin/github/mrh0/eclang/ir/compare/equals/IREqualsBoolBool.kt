package github.mrh0.eclang.ir.compare.equals

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.context.Context

class IREqualsBoolBool(location: Loc, private val left: IIR, private val right: IIR) : IR(location) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        return GsBool((left.evaluate(vm, c) as GsBool).value == (right.evaluate(vm, c) as GsBool).value)
    }

    override fun toString() = "($left b==b $right)"
}
