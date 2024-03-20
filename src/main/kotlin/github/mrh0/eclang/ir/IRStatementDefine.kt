package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc

class IRStatementDefine(location: Loc, private val index: Int, private val expr: IIR) : IR(location) {
    override fun toString(): String {
        return "define($$index, $expr)"
    }
}