package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRLong
import github.mrh0.eclang.ir.data.IRShort
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort

class TShort(location: Loc, private val value: Short) : Tok(location) {
    override fun toString(): String {
        return "${value}s"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeShort to IRShort(location, value)
    }
}