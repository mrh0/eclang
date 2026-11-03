package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer
import github.mrh0.eclang.util.Util

class TTypePointerByName(location: Loc, val type: ITok) : Tok(location) {
    override fun toString() = "@$type"

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = type.process(cd, hint)
        val builtIn = EcTypePointer.of(res.first)
        return Pair(builtIn, IRType(location, builtIn))
    }
}