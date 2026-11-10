package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/** `use "define:NAME"` or `use "define:NAME=value"`: a C `#define`, written before every `#include` (see `IRProgram`). */
class IRDefine(location: Loc, val name: String, val value: String?) : IR(location) {
    override fun toString(): String = "define($name)"

    fun line() = if (value == null) "#define $name" else "#define $name $value"

    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putLine(line())
    }

    override fun isConstant(): Boolean = true
}
