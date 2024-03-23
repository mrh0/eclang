package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRString
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeString

class TString(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "'${value}'"
    }

    override fun process(cd: CompileData): Pair<EcType, IIR> {
        return EcTypeString to IRString(location, value)
    }
}