package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.function.FunctionParameter
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.internal.IEcTypeDefaultArgumentWrapper
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.EcTypeUnion


class TParameterTypedDefault(location: Loc, name: String, val type: ITok, val def: ITok) : TParameter(location, name) {
    override fun toString() = "$name:$type"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> = throw NotImplementedError()

    override fun toFunctionParameter(cd: CompileData): FunctionParameter {
        testIdentifier(location, name)
        //val defPair = def.process(cd, EcTypeNone)
        //if (!defPair.second.deterministic()) throw EcError(location, "Default value of $name must be deterministic.")
        val typePair = type.process(cd, EcTypeNone)
        //if (!typePair.first.accepts(location, defPair.first)) throw EcAssignTypeError(location, name, typePair.first, defPair.first)
        return FunctionParameter(name, EcTypeUnion(mutableSetOf(typePair.first, IEcTypeDefaultArgumentWrapper(typePair.first))) , def)
    }
}