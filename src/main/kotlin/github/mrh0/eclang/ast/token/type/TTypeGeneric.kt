package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeGeneric

class TTypeGeneric(location: Loc, val name: String) : Tok(location) {
    override fun toString() = "<$name>"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val type = EcTypeGeneric(name)
        return Pair(type, IRType(location, type))
    }
}