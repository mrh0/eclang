package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * `((T)(expr))`, or `((void*)(expr))` when [type] is null. Used where eclang meets C declarations that differ
 * only in `const`, which eclang doesn't have: C won't pass a `char**` as a `const char**`.
 */
class IRCCast(location: Loc, private val expr: IIR, private val type: Type?) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("((${type?.let { sb.types.cName(it) } ?: "void*"})(")
        expr.toC(sb, c)
        sb.put("))")
    }

    override fun toString() = "(${type ?: "void*"})$expr"
}
