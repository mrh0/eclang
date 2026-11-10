package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder
import github.mrh0.ect2.Type

/** `T name = expr;` for a generated temporary. */
class IRTempDefine(location: Loc, val name: String, val type: Type, val expr: IIR) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.putStatement("${sb.types.cName(type)} $name = ")
        expr.toC(sb, c)
        sb.endStatement()
    }

    override fun toString() = "Temp($name = $expr)"
}
