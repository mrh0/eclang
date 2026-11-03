package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNop(location: Loc) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {}

    override fun toString(): String {
        return "IRNop()"
    }
}