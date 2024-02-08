package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.branch.IRStatementIf
import github.mrh0.eclang.ir.branch.IRStatementIfElse
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeBool
import github.mrh0.eclang.types.EcTypeNone

class TStatementIf(location: Loc, private val conditions: List<ITok>, private val bodies: List<ITok>, private val elseBody: ITok?) : Tok(location) {
    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val conditionPairs = conditions.map { it.process(cd) }
        conditionPairs.forEach { if(!EcTypeBool.accepts(location, it.first)) throw EcError(location, "Expected if statement condition to be a boolean.") }
        val conditionIRs = conditionPairs.map { it.second }
        val bodyIRs = bodies.map { it.process(cd).second }

        if(elseBody != null) {
            return Pair(EcTypeNone, IRStatementIfElse(location, conditionIRs, bodyIRs, elseBody.process(cd).second))
        }
        return Pair(EcTypeNone, IRStatementIf(location, conditionIRs, bodyIRs))
    }

    override fun toString(): String {
        return "TIf($conditions, $bodies, else: $elseBody)"
    }
}
