package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRDeadEnd
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcTypeAny

class TTypeByName (location: Loc, val type: String) : Tok(location) {
    override fun toString() = "#$type"

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> = Pair(BuiltInTypes.getType(type), IRDeadEnd)
}