package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.types.BuiltInTypes
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer
import github.mrh0.eclang.util.Util

class TTypeAddressByName(location: Loc, val type: String) : Tok(location) {
    override fun toString() = type

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = Util.testIdentifier(location, type.substring(1))
        val builtIn = EcTypePointer(BuiltInTypes.getType(fixedName))
        return Pair(builtIn, IRType(location, builtIn))
    }
}