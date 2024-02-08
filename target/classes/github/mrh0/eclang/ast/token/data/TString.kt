package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRValue
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeString
import github.mrh0.eclang.values.GsString

class TString(location: Loc, private val value: String) : Tok(location) {
    override fun toString(): String {
        return "'${value}'"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return EcTypeString to IRValue(location, GsString(value))
    }
}