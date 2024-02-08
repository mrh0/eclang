package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.context.Context

class IRValue<T : GsBase>(location: Loc, val value: T) : IR(location) {
    override fun toString(): String {
        return "value(${value})"
    }

    override fun evaluate(vm: VM, c: Context): T {
        return value
    }

    override fun deterministic() = true
}