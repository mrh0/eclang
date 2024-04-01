package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignTypeError
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone
import github.mrh0.eclang.types.EcTypeUnion

class TParameter(location: Loc, val name: String, val type: ITok?, val def: ITok?) : Tok(location) {
    override fun toString() = "$name:$type"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        if (type == null && def == null) throw EcError(location, "Cannot determine type of parameter $name.")
        if (def != null) {
            val defPair = def.process(cd)
            if (!defPair.second.deterministic()) throw EcError(location, "Default value of $name must be deterministic.")
            if (type != null) {
                val typePair = type.process(cd)
                if (!typePair.first.accepts(location, defPair.first)) throw EcAssignTypeError(location, name, typePair.first, defPair.first)
                return typePair.first to IRParameter(location, name, EcTypeUnion(mutableSetOf(typePair.first, EcTypeNone)), defPair.second)
            }
            return defPair.first to IRParameter(location, name, EcTypeUnion(mutableSetOf(defPair.first, EcTypeNone)), defPair.second)
        }
        val typePair = type!!.process(cd)
        return typePair.first to IRParameter(location, name, typePair.first, null)
    }
}