package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.function.IRFunc
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.values.GsValueNone
import github.mrh0.eclang.context.state.Variable

class TLambda(location: Loc, val expr: ITok, val args: MutableList<TArgument>, val returns: ITok) : Tok(location) {
    override fun toString() = "TLambda($args, $expr)"

    private var funcIR: IRFunc? = null
    fun getFuncIR() = funcIR ?: throw Exception("User function supplier is empty. This should never happen!")
    fun processSignature(cd: CompileData): Pair<List<Pair<String, EcTypeAny>>, EcTypeAny> {
        val argPairs = args.map { Pair(it.name, it.process(cd).first) }
        val returnType = returns.process(cd).first
        return Pair(argPairs, returnType)
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        //cd.newContext(name)
        val argPairs = args.map { Pair(it.name, it.process(cd).first) }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second, GsValueNone)) }
        val returnType = returns.process(cd).first
        val ir = expr.process(cd)
        funcIR = IRFunc(location, ir.second as IRBlock, "name", argPairs, returnType)
        return Pair(EcTypeNone, funcIR!!)
    }
}