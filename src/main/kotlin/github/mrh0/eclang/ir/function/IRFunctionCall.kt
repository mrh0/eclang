package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IR

class IRFunctionCall(location: Loc, val name: String, val contextIndex: Int, val args: List<IIR>) : IR(location) {
    override fun toString(): String {
        return "$name($args)"
    }
}