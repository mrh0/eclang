package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.function.IRFuncExternal

class TFuncExternal(location: Loc, name: String, args: TParameters, returns: ITok, val externalName: String) : TFunc(location, name, args, returns) {
    override fun toString() = "external:$name'$externalName'($args)"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        cd.newContext(name)
        val argPairs = args.get().map { Pair(it.name, it.process(cd).first) }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second)) }
        val returnType = returns.process(cd).first
        return Pair(EcTypeNone, IRFuncExternal(location, name, externalName))
    }

    override fun getSourceName() = externalName
}