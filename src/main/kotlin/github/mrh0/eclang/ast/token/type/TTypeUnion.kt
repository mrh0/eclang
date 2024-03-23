package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeUnion

class TTypeUnion(location: Loc, val types: List<ITok>) : Tok(location) {
    override fun toString() = "#Union($types)"

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val union = EcTypeUnion(types.map { it.process(cd) })
        return Pair(union, IRType(location, union))
    }
}