package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.context.Context

class IRStatementWhileElse(location: Loc, private val condition: IIR, private val body: IIR, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "whileElse($condition, $body)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        var result: GsBase? = null
        while((condition.evaluate(vm, c) as GsBool).value && !c.isBreakFlagged() && !c.isReturnFlagged()) {
            result = body.evaluate(vm, c)
            c.captureContinue()
        }
        c.captureBreak()
        return result ?: elseBody.evaluate(vm, c)
    }
}