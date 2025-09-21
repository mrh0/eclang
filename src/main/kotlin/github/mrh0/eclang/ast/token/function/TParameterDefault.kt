package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.internal.EcTypeDefaultParameterWrapper
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.EcTypeUnion


class TParameterDefault(location: Loc, name: String, val def: ITok) : TParameter(location, name) {
    override fun toString() = name

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError()

    override fun toFunctionParameter(cd: CompileData): FunctionParameter {
        val fixedName = testIdentifier(location, name)
        val typePair = def.process(cd, EcTypeNone)
        return FunctionParameter(fixedName, EcTypeUnion.of(typePair.first, EcTypeDefaultParameterWrapper(typePair.first)) , def)
    }
}