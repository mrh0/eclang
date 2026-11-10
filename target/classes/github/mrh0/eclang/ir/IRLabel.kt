package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `label:;`, where a `goto` jumps to. The `;` lets it end a block or come before a declaration. */
class IRLabel(location: Loc, val label: String) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) = sb.putStatement("$label:;\n")

    override fun toString() = "$label:"
}
