package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUnsignedInteger

class TUnsignedInteger(location: Loc, private val value: Int) : Tok(location) {
    override fun toString(): String {
        return "${value}ui"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeUInt to IRInt(location, value)
    }
}