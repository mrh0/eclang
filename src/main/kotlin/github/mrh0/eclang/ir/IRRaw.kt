package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** Literal C text, used for generated temporaries such as `__ec_tmp0.value`. */
class IRRaw(location: Loc, val text: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put(text)
    }

    override fun toString() = text
}
