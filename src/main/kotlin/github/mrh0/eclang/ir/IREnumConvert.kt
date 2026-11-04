package github.mrh0.eclang.ir

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.global.CEnum
import github.mrh0.eclang.context.Context
import github.mrh0.eclang.output.c.CSourceBuilder

/**
 * Converts between an atom of [enum] and its C constant, where eclang calls a `declare fn`: an argument
 * [toC], a returned value back to the atom (stopping the program if C returns a value the enum doesn't list).
 */
class IREnumConvert(location: Loc, private val enum: CEnum, private val expr: IIR, private val toC: Boolean) : IR(location) {
    override fun toC(sb: CSourceBuilder, c: Context) {
        sb.put("${if (toC) sb.types.enumToC(enum.cName, enum.members) else sb.types.enumFromC(enum.cName, enum.name, enum.members)}(")
        expr.toC(sb, c)
        if (!toC) sb.put(", ${IRConvert.where(location)}")
        sb.put(")")
    }

    override fun toString() = "${if (toC) "toC" else "fromC"}:${enum.name}($expr)"
}
