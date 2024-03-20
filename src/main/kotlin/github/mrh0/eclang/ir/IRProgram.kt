package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.function.IRFunc
import github.mrh0.eclang.output.c.CSourceBuilder

class IRProgram(location: Loc, functions: List<IRFunc>) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "IRProgram"
    }
}