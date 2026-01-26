package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.CompileData
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
import github.mrh0.eclang.ir.data.IRCast
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.record.IRCreateRecord
import github.mrh0.eclang.ir.data.record.IRNamedStructAssignment
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeResult

class TStatementThrow(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        if (hint !is EcTypeResult) TODO("TODO: Type is not Catchable")
        val res = next.process(cd, hint)
        if (!hint.accepts(location, res.first)) throw EcReturnTypeError(location, hint, res.first)
        cd.ctx().setReturned()

        val ir = IRCreateRecord(
            location,
            ResultInstance.get(hint.returnedType, hint.throwableType).getId(),
            listOf(
                IRInt(location, 1),
                IRNamedStructAssignment(location, "error", res.second)
            )
        )

        if (cd.ctx().getDeferred().isEmpty()) return Pair(res.first, IRStatementReturn(location, ir))
        return res.first to IRStatementReturnDefer(
            location,
            ir,
            IRType(location, res.first),
            cd.ctx().getDeferred().map { it.process(cd, hint).second }
        )
    }

    override fun toString(): String {
        return "TRet($next)"
    }
}