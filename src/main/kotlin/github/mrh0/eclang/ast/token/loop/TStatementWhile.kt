package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRStatementWhile
import github.mrh0.eclang.ir.loop.IRStatementWhileElse
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TStatementWhile(location: Loc, private val condition: ITok, private val body: ITok, private val elseBody: ITok?) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val conditionIR = condition.process(cd, hint)
        val bodyIR = body.process(cd, hint)

        if(elseBody != null) {
            return Pair(EcTypeNone, IRStatementWhileElse(location, conditionIR.second, bodyIR.second, elseBody.process(cd, hint).second))
        }
        return Pair(EcTypeNone, IRStatementWhile(location, conditionIR.second, bodyIR.second))
    }

    override fun toString(): String {
        return "TWhile($condition, $body, else: $elseBody)"
    }
}
