package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRShort
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUShort
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUnsignedInteger

class TUnsignedShort(location: Loc, private val value: Short) : Tok(location) {
    override fun toString(): String {
        return "${value}us"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeUShort to IRShort(location, value)
    }
}