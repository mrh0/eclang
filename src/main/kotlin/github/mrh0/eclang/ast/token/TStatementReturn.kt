package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.types.EcTypeAny

class TStatementReturn(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val ir = next.process(cd)
        return Pair(ir.first, IRStatementReturn(location, ir.second))
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}