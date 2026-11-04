package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.Type

/** An integer literal. Its type is decided by where it is used (see IntLiteral); on its own it is an Int. */
class TInteger(location: Loc, val value: Long) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type = cd.types.intLiteral(value)
        return type to IRNumber(location, value, type)
    }

    override fun toString(): String {
        return "TInteger($value)"
    }
}
