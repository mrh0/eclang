package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

class IRInclude(location: Loc, private val path: String) : IR(location) {
    override fun toString(): String {
        return "include($path)"
    }

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putLine("#include <$path>")
    }

    override fun deterministic(): Boolean = true
}