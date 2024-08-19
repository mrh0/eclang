package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRBool
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeBool

class TBoolean(location: Loc, private val value: Boolean) : Tok(location) {
    override fun toString(): String {
        return "$value"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeBool to IRBool(location, value)
    }
}