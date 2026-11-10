package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `#define NAME (value)` for a top-level `const`. */
class IRDefineConstant(location: Loc, private val name: String, private val value: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("#define $name (")
        value.toC(sb, c)
        sb.put(")\n")
    }

    override fun toString() = "#define $name $value"
}
