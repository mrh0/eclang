package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRCast
import github.mrh0.eclang.types.EcType

class TCast(location: Loc, val expr: ITok, val type: ITok) : Tok(location) {
    override fun toString() = "TCast($type)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val exprRes = expr.process(cd, hint)
        val typeRes = type.process(cd, hint)
        val casted = exprRes.first.cast(location, typeRes.first)
        return casted to IRCast(location, typeRes.second, exprRes.second)
    }
}