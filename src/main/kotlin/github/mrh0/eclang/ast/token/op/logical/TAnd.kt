package github.mrh0.eclang.ast.token.op.logical

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.arithmetic.add.IRAdd
import github.mrh0.eclang.ir.logical.and.IRAnd
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool

class TAnd(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left and $right)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val l = left.process(cd, hint);
        val r = right.process(cd, hint);
        return when {
            // Numbers
            l.first is EcTypeBool || r.first is EcTypeBool -> Pair(EcTypeBool, IRAnd(location, l.second, r.second))
            else -> throw EcOpTypeError(location, "and", l.first, r.first)
        }
    }
}
