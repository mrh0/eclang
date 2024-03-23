package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.branch.IRInlineIf
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool

class TInlineIf(location: Loc, private val condition: ITok, private val body: ITok, private val elseBody: ITok) : Tok(location) {
    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val conditionPair = condition.process(cd)
        val bodyPair = body.process(cd)
        val elseBodyPair = elseBody.process(cd)
        if(!EcTypeBool.accepts(location, conditionPair.first)) throw EcError(location, "Expected if statement condition to be a boolean.")
        if(!bodyPair.first.accepts(location, elseBodyPair.first)) throw EcError(location, "Expected both if and else body to return same type.")

        return Pair(bodyPair.first, IRInlineIf(location, conditionPair.second, bodyPair.second, elseBodyPair.second))
    }

    override fun toString(): String {
        return "TIf($condition, $body, $elseBody)"
    }
}
