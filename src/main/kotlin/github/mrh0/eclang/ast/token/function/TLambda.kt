package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters

class TLambda(location: Loc, val expr: ITok, val args: MutableList<TParameter>, val returns: ITok) : Tok(location) {
    override fun toString() = "TLambda($args, $expr)"

    private var funcIR: IRFunctionOverride? = null
    fun getFuncIR() = funcIR ?: throw Exception("User function supplier is empty. This should never happen!")
    fun processSignature(cd: CompileData): Pair<List<Pair<String, EcType>>, EcType> {
        val argPairs = args.map { it.name to it.process(cd, EcTypeNone).first }
        val returnType = returns.process(cd, EcTypeNone).first
        return argPairs to returnType
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        //cd.newContext(name)
        val argPairs = args.map { Pair(it.name, it.process(cd, hint).first) }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second)) }
        val returnType = returns.process(cd, hint).first
        val ir = expr.process(cd, hint)
        funcIR = IRFunctionOverride(location, ir.second as IRBlock, "name", IRParameters(location, argPairs.map { IRParameter(location, it.first, it.second, null) }), returnType, null, null)
        return EcTypeNone to funcIR!!
    }
}