package github.mrh0.eclang.ast.token.op.bitwise

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.bitwise.IRBitwiseNot
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `~x`: flips every bit of a whole number. */
class TBitwiseNot(location: Loc, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val (t, ir) = cd.usable(location, expr.process(cd, Types.None), "`~`")
        if (t is IntLiteral) return cd.types.intLiteral(t.value.inv()).let { it to IRNumber(location, t.value.inv(), it) }
        if (!Ops.isInteger(t)) throw EcError(location, "`~` needs a whole number, but got $t.${if (t === Types.Bool) " For a Bool use `not`." else ""}")
        val type = t as Prim
        return type to IRBitwiseNot(location, ir, if (type.bits < 32) type else null)
    }

    override fun toString() = "TBitwiseNot($expr)"
}
