package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone


class TParameterTyped(location: Loc, name: String, val type: ITok) : TParameter(location, name) {
    override fun toString() = "$name:$type"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError()

    override fun toFunctionParameter(cd: CompileData): FunctionParameter {
        testIdentifier(location, name)
        val typePair = type.process(cd, EcTypeNone)
        return FunctionParameter(name, typePair.first, null)
    }
}