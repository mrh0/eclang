package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.types.EcType

abstract class TFunc(location: Loc, val name: String, val params: TParameters, val returns: ITok) : Tok(location) {
    override fun toString() = "SHOULD NOT HAPPEN"

    fun processSignature(cd: CompileData): Pair<List<FunctionParameter>, EcType> {
        val argPairs = params.get().map {
            val v = it.process(cd)
            FunctionParameter(it.name, v.first, v.second)
        }
        val returnType = returns.process(cd).first

        return argPairs to returnType
    }

    open fun getSourceName(): String? = null
}