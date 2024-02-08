package github.mrh0.eclang.ast.token.op.arithmetic

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcOpTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcTypeAny

class TMul(location: Loc, val left: ITok, val right: ITok) : Tok(location) {
    override fun toString(): String {
        return "($left * $right)"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val l = left.process(cd);
        val r = right.process(cd);
        return when {
            else -> throw EcOpTypeError(location, "*", l.first, r.first)
        }
    }
}
