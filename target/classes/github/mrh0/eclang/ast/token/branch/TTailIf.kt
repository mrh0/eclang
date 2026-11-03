package github.mrh0.eclang.ast.token.branch

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.branch.IRTailIf
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool

class TTailIf(location: Loc, private val body: ITok, private val condition: ITok?) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        if (condition == null) return body.process(cd, hint)
        val conditionPair = condition.process(cd, hint)
        val bodyPair = body.process(cd, hint)
        if(!EcTypeBool.accepts(location, conditionPair.first)) throw EcError(location, "Expected when expression condition to be a boolean.")

        return bodyPair.first to IRTailIf(location, bodyPair.second, conditionPair.second)
    }

    override fun toString(): String = "TTailIf($condition, $body)"
}
