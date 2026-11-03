package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType

class TTypeByName(location: Loc, val type: String) : Tok(location) {
    override fun toString() = "#$type"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val builtIn = BuiltInTypes.getType(type)
        return Pair(builtIn, IRType(location, builtIn))
    }
}