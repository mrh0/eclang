package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRPass(location: Loc, private val comment: String?) : IR(location) {
    override fun toString(): String {
        return "Pass"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        if (comment != null) sb.comment(comment)
    }

    override fun deterministic(): Boolean = true
}