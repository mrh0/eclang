package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc

class IRStatementReturn(location: Loc, val next: IIR) : IR(location) {
    override fun toString(): String {
        return "ret($next)"
    }
}