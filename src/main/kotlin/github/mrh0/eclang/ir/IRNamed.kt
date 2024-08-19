package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRNamed(location: Loc, private val name: String, private val index: Int) : IR(location) {
    override fun toString(): String {
        return "$$index"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(name)
    }
}