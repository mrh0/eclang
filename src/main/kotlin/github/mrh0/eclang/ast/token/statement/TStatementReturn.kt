package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.tuple.TupleInstance
import github.mrh0.eclang.error.EcReturnTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.IRStatementReturnDefer
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.data.IRNull
import github.mrh0.eclang.ir.data.IRValue
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCatchable

class TStatementReturn(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        var hint2 = hint
        if (hint is EcTypeCatchable) hint2 = hint.returnedType
        val res = next.process(cd, hint2)
        if (!hint2.accepts(location, res.first)) throw EcReturnTypeError(location, hint2, res.first)
        cd.ctx().setReturned()

        var ir = res.second
        if (hint is EcTypeCatchable) ir = IRCreateRecord(location, TupleInstance.get(hint.types).getId(), listOf(res.second, IRNull(location)))

        if (cd.ctx().getDeferred().isEmpty()) return Pair(res.first, IRStatementReturn(location, ir))
        return res.first to IRStatementReturnDefer(
            location,
            ir,
            IRType(location, res.first),
            cd.ctx().getDeferred().map { it.process(cd, hint2).second }
        )
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}