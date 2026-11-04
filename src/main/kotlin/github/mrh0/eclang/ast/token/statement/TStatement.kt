package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatement
import github.mrh0.ect2.Result
import github.mrh0.ect2.Type

/** An expression used as a statement; its value is discarded. */
class TStatement(location: Loc, val next: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val ir = next.process(cd, hint)
        if (ir.first is Result) throw EcError(location, "The error from this call is ignored: ${cd.types.explain(ir.first, cd.types.concrete((ir.first as Result).value))}.")
        if (next is TStatementReturn || next is TStatementThrow || ir.second is IRStatement) return ir
        return Pair(ir.first, IRStatement(location, ir.second))
    }

    override fun toString(): String {
        return "TStatement($next)"
    }
}
