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

class TFuncBlock (location: Loc, val block: TBlock, name: String, params: TParameters, returns: ITok) : TFunc(location, name, params, returns) {
    override fun toString() = "$name($params, $block)"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError("Should not be implemented")

    /*
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        cd.newContext(name)
        val argPairs = params.get().map { it.name to it.process(cd, hint).first }.toTypedArray()
        argPairs.forEach { cd.ctx().define(location, Variable(it.first, it.second)) }
        val returnType = returns.process(cd, hint).first
        val ir = block.process(cd, hint)

        val overrides = GlobalFunctions.getOverridesByName(location, name).overrides
        val functionOverrides = overrides.map { override ->
            IRFunctionOverride(location, ir.second as IRBlock, name, IRParameters(location, override.params.map { IRParameter(location, it.name, it.type, it.def) }), returnType)
        }

        return EcTypeNone to IRFunctionOverrides(location, name, functionOverrides)
    }
    */

    override fun getSourceName(): String? {
        return if (name == "main") "main" else null // TODO: Change
    }
}