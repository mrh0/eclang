package github.mrh0.eclang.ir.function

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.ir.IR
import github.mrh0.eclang.output.c.CSourceBuilder

class IRFunctionExternal(location: Loc, val name: String, val externalName: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.commentLine("external fn $name '$externalName'.")
    }
}