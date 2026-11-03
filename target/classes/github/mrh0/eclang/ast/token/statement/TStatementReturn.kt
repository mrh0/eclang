package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.result.ResultInstance
import github.mrh0.eclang.context.tuple.TupleInstance
import github.mrh0.eclang.error.fn.EcReturnTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementReturn
import github.mrh0.eclang.ir.IRStatementReturnDefer
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRNull
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.ir.data.record.IRNamedStructAssignment
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeResult

class TStatementReturn(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        var hint2 = hint
        if (hint is EcTypeResult) hint2 = hint.returnedType
        val res = next.process(cd, hint2)
        if (!hint2.accepts(location, res.first)) throw EcReturnTypeError(location, hint2, res.first)
        cd.ctx().setReturned()

        var ir = res.second
        if (hint is EcTypeResult) ir = IRCreateRecord(
            location,
            ResultInstance.get(hint.returnedType, hint.throwableType).getId(),
            listOf(
                IRInt(location, 0),
                IRNamedStructAssignment(location, "value", res.second)
            )
        )

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