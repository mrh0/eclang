package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.types.EcType

abstract class TFunc(location: Loc, val name: String, val args: TParameters, val returns: ITok) : Tok(location) {
    override fun toString() = "SHOULD NOT HAPPEN"

    fun processSignature(cd: CompileData): Pair<List<Pair<String, EcType>>, EcType> {
        val argPairs = args.get().map { Pair(it.name, it.process(cd).first) }
        val returnType = returns.process(cd).first
        return Pair(argPairs, returnType)
    }

    open fun getSourceName(): String? = null
}