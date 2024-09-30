package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.*

class TTypeArray(location: Loc, val type: ITok) : Tok(location) {
    override fun toString() = "$type[]"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = type.process(cd, hint)
        val array = EcTypeArray(res.first)
        return array to IRType(location, array)
    }
}