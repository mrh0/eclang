package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRCString
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeCString

class TCString(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "c'${value}'"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeCString to IRCString(location, value)
    }
}