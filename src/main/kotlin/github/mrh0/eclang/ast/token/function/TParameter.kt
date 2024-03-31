package github.mrh0.eclang.ast.token.function

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.function.IRParameter
import github.mrh0.eclang.types.EcType

class TParameter(location: Loc, val name: String, val type: ITok?, val def: ITok?) : Tok(location) {
    override fun toString() = "$name:$type"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        if (type == null && def == null) throw EcError(location, )
        type.process(cd).first to IRParameter(location, name, type.process(cd).first)
    }
}