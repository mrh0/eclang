package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcReturnTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.IRStatementReturnDefer
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.EcType

class TStatementReturn(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val ir = next.process(cd, hint)
        if (!hint.accepts(location, ir.first)) throw EcReturnTypeError(location, hint, ir.first)
        cd.ctx().setReturned()

        if (cd.ctx().getDeferred().isEmpty()) return Pair(ir.first, IRStatementReturn(location, ir.second))
        return ir.first to IRStatementReturnDefer(
            location,
            ir.second,
            IRType(location, ir.first),
            cd.ctx().getDeferred().map { it.process(cd, hint).second }
        )
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}