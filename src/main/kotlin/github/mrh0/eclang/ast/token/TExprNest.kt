package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRExprNest
import github.mrh0.ect2.IntLiteral
import github.mrh0.ect2.FloatLiteral
import github.mrh0.ect2.Type

class TExprNest(location: Loc, val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val res = expr.process(cd, hint)
        // Keep literals bare so they can still be folded and converted, e.g. `(2 + 3) * x`.
        if (res.first is IntLiteral || res.first is FloatLiteral) return res
        return Pair(res.first, IRExprNest(location, res.second))
    }

    override fun toString(): String {
        return "TExprNest($expr)"
    }
}
