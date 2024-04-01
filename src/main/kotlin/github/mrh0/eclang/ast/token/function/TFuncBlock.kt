package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.token.TBlock
import github.mrh0.eclang.context.function.GlobalFunctions
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.ir.function.IRFunctionOverrides
import github.mrh0.eclang.ir.function.IRFunctionOverride
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.ir.function.IRParameters
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TFuncBlock (location: Loc, val block: TBlock, name: String, args: TParameters, returns: ITok) : TFunc(location, name, args, returns) {
    override fun toString() = "$name($args, $block)"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        cd.newContext(name)
        val argPairs = args.get().map { it.name to it.process(cd).first }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second)) }
        val returnType = returns.process(cd).first
        val ir = block.process(cd)

        val overrides = GlobalFunctions.getOverridesByName(location, name).overrides
        val functionOverrides = overrides.map {
            IRFunctionOverride(location, ir.second as IRBlock, name, IRParameters(location, argPairs.map { IRParameter(location, it.first, it.second, null) }), returnType)
        }

        return EcTypeNone to IRFunctionOverrides(location, name, functionOverrides)
    }

    override fun getSourceName(): String? {
        return if (name == "main") "main" else null // TODO: Change
    }
}