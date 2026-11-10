package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/**
 * A return with deferred statements: the value is computed first, then the deferred statements run, then it is
 * returned. `T tmp = next; <deferred>; return tmp;`
 */
class IRStatementReturnDefer(location: Loc, val next: IIR?, val type: Type, val deferred: List<IIR>, val temp: String, val hasValue: Boolean = true) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        if (next != null) {
            sb.putStatement(if (hasValue) "${sb.types.cName(type)} $temp = " else "")
            next.toC(sb, c)
            sb.endStatement()
        }
        deferred.forEach { it.toC(sb, c) }
        sb.putStatement(if (next != null && hasValue) "return $temp" else "return")
        sb.endStatement()
    }

    override fun toString(): String {
        return "ret($next)"
    }
}
