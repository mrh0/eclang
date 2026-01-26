package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.function.IRFunctionExternal

class TFuncExternal(location: Loc, name: String, params: TParameters, returns: ITok?, private val externalName: String?) : TFunc(location, name, params, returns) {
    override fun toString() = "external:$name'$externalName'($params)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        /*cd.newContext(name)
        val argPairs = params.get().map { it.name to it.process(cd, hint).first }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second)) }
        val returnType = returns?.process(cd, hint)?.first ?: EcTypeNone*/
        return EcTypeNone to IRFunctionExternal(location, name, externalName ?: name)
    }

    override fun getSourceName() = externalName
}