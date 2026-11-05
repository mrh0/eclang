package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/**
 * An integer literal. Its type is decided by where it is used (see IntLiteral); on its own it is an Int. A hexadecimal
 * or binary literal above the largest Long has its top bit in [value], which reads as negative: only a ULong holds it.
 */
class TInteger(location: Loc, val value: Long) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        if (value < 0) return Types.ULong to IRNumber(location, java.lang.Long.toUnsignedString(value).toBigInteger(), Types.ULong)
        val type = cd.types.intLiteral(value)
        return type to IRNumber(location, value, type)
    }

    override fun toString(): String {
        return "TInteger($value)"
    }
}
