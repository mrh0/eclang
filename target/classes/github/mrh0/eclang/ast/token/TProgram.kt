package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.function.TFunc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.IRFunc
import github.mrh0.eclang.ir.IRProgram
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeGlobalFunction
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.values.GsGlobalFunction
import github.mrh0.eclang.vm.function.FunctionManager
import github.mrh0.eclang.vm.function.UserCallable
import github.mrh0.eclang.vm.state.GlobalFunctionReference

class TProgram(location: Loc, private val functions: MutableList<TFunc>, val uses: List<ITok>) : Tok(location) {

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        uses.map { it.process(cd) }
        functions.map { analyzeFunction(it, cd) }
        val irs = functions.map { it.process(cd).second as IRFunc }
        return Pair(EcTypeNone, IRProgram(location, irs))
    }

    private fun analyzeFunction(func: TFunc, cd: CompileData) {
        val res = func.processSignature(cd)
        val argNames = res.first.map { it.first }.toTypedArray()
        val argTypes = res.first.map { it.second }.toTypedArray()
        val retType = res.second
        val fos = FunctionManager.INSTANCE.addOverride(func.location, func.name, argNames, argTypes, retType, UserCallable(func::getFuncIR))
        if(fos.getNumberOfOverrides() == 1)
            cd.getGlobal().define(location, GlobalFunctionReference(func.name, EcTypeGlobalFunction, GsGlobalFunction(fos)))
    }

    override fun toString(): String {
        return "TProgram$functions"
    }
}