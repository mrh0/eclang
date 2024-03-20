package github.mrh0.eclang.ir.loop

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IR

class IRBreak(location: Loc) : IR(location) {
    override fun toString(): String {
        return "break"
    }
}