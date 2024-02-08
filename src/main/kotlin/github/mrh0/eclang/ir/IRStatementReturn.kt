package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.context.Context

class IRStatementReturn(location: Loc, val next: IIR) : IR(location) {
    override fun toString(): String {
        return "ret($next)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        val ret = next.evaluate(vm, c)
        c.flagReturn()
        return ret
    }
}