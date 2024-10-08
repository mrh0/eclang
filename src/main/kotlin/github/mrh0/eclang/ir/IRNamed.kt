package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNamed(location: Loc, private val name: String) : IR(location) {
    override fun toString(): String {
        return "$$name"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(name)
    }

    override fun deterministic(): Boolean {
        return super.deterministic()
    }
}