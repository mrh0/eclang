package github.mrh0.eclang.ast.token

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.IRExprNest
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TExprNest(location: Loc, val expr: ITok) : Tok(location) {
    override fun toString(): String {
        return "$expr"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = expr.process(cd, hint)
        return Pair(res.first, IRExprNest(location, res.second))
    }
}