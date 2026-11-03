package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRLong
import github.mrh0.eclang.ir.data.IRShort
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUInt
import github.mrh0.eclang.types.numbers.unsigned.EcTypeULong
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUShort
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUnsignedInteger

class TUnsignedLong(location: Loc, private val value: Long) : Tok(location) {
    override fun toString(): String {
        return "${value}ul"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeULong to IRLong(location, value)
    }
}