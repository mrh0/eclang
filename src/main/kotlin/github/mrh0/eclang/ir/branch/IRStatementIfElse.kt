package github.mrh0.eclang.ir.branch

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsBool
import github.mrh0.eclang.context.Context

class IRStatementIfElse(location: Loc, private val conditions: List<IIR>, private val bodies: List<IIR>, private val elseBody: IIR) : IR(location) {
    override fun toString(): String {
        return "if($conditions, $bodies, else: $elseBody)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        for (i in conditions.indices) {
            if((conditions[i].evaluate(vm, c) as GsBool).value) {
                return bodies[i].evaluate(vm, c)
            }
        }
        return elseBody.evaluate(vm, c)
    }
}