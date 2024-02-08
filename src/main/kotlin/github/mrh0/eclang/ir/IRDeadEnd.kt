package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.context.Context

object IRDeadEnd : IR(Loc.IDENTITY) {
    override fun evaluate(vm: VM, c: Context): GsBase {
        throw Exception("evaluating dead end should not occur")
    }
}