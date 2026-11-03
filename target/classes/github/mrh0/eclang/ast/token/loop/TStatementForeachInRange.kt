package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.loop.IRStatementForeachInRange
import github.mrh0.eclang.ir.loop.IRStatementWhile
import github.mrh0.eclang.ir.loop.IRStatementWhileElse
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.numbers.EcTypeNumber
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

class TStatementForeachInRange(location: Loc, private val varName: String, private val lower: ITok, private val upper: ITok, private val body: ITok, private val elseBody: ITok?) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        cd.ctx().define(location, Variable(varName, EcTypeInt))
        val lowerIR = lower.process(cd, hint)
        val upperIR = upper.process(cd, hint)
        val bodyIR = body.process(cd, hint)

        if (lowerIR.first !is EcTypeNumber) throw EcUnexpectedTypeError(location, EcTypeInt, lowerIR.first)
        if (upperIR.first !is EcTypeNumber) throw EcUnexpectedTypeError(location, EcTypeInt, upperIR.first)

        return Pair(EcTypeNone, IRStatementForeachInRange(location, varName, IRType(location, EcTypeInt), lowerIR.second, upperIR.second, bodyIR.second))
    }

    override fun toString(): String {
        return "TFor($varName, $lower, $upper, $body, else: $elseBody)"
    }
}
