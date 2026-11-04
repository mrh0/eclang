package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.op.Ops
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.negate.IRNegate
import github.mrh0.eclang.ir.data.IRNumber
import github.mrh0.ect2.FloatLiteral
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.Prim
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TNegate(location: Loc, val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val e = cd.usable(location, expr.process(cd, Types.None), "`-`")
        return when (val t = e.first) {
            is IntLiteral -> cd.types.intLiteral(-t.value).let { it to IRNumber(location, -t.value, it) }
            is FloatLiteral -> cd.types.floatLiteral(-t.value).let { it to IRNumber(location, -t.value, it) }
            else -> {
                if (!Ops.isNumeric(t)) throw EcError(location, "`-` needs a number, but got $t.")
                if ((Ops.base(t) as? Prim)?.category == github.mrh0.ect2.Category.UINT) throw EcError(location, "$t can't be negative. Convert it first, e.g. `-(x as Long)`.")
                t to IRNegate(location, e.second)
            }
        }
    }

    override fun toString(): String {
        return "TNegate($expr)"
    }
}
