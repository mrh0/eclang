package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRBlock(location: Loc, val statements: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "$statements"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        for (statement in statements) {
            val ret = statement.evaluate(vm, c)
            if(c.isReturnFlagged()) return ret
            if(c.isContinueFlagged() || c.isBreakFlagged()) return GsValueNone
        }
        return GsValueNone
    }
}