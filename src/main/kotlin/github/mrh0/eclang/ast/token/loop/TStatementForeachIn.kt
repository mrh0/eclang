package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.error.EcUnexpectedTypeError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.accessor.IRAccessorNamed
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRValue
import github.mrh0.eclang.ir.loop.IRStatementForeachInRange
import github.mrh0.eclang.ir.loop.IRStatementWhile
import github.mrh0.eclang.ir.loop.IRStatementWhileElse
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeArray
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.numbers.EcTypeNumber
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

class TStatementForeachIn(location: Loc, private val varName: String, private val iterable: ITok, private val body: ITok, private val elseBody: ITok?) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        cd.ctx().define(location, Variable(varName, EcTypeInt))
        val iterableIR = iterable.process(cd, hint)
        val bodyIR = body.process(cd, hint)

        if (iterableIR.first !is EcTypeArray) throw EcUnexpectedTypeError(location, EcTypeInt, iterableIR.first)

        return Pair(EcTypeNone, IRStatementForeachInRange(location, varName, IRType(location, EcTypeInt), IRInt(location, 0), IRAccessorNamed(location, iterableIR.second, "len"), bodyIR.second))
    }

    override fun toString(): String {
        return "TFor($varName, $iterable, $body, else: $elseBody)"
    }
}
