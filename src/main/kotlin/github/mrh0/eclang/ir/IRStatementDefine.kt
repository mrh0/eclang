package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.values.GsBase
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.context.Context

class IRStatementDefine(location: Loc, private val index: Int, private val expr: IIR) : IR(location) {
    override fun toString(): String {
        return "define($$index, $expr)"
    }

    override fun evaluate(vm: VM, c: Context): GsBase {
        c.setValue(location, index, expr.evaluate(vm, c))
        return GsValueNone
    }
}