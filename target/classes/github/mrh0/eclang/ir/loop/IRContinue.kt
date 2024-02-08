package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.vm.Context
import github.mrh0.eclang.vm.VM

class IRContinue(location: Loc) : IR(location) {
    override fun toString(): String {
        return "continue"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        c.flagContinue()
        return GsValueNone
    }
}