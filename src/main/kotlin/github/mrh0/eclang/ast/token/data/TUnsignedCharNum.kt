package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCast
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeChar
import github.mrh0.eclang.types.numbers.unsigned.EcTypeUChar

class TUnsignedCharNum(location: Loc, private val value: Int) : Tok(location) {
    override fun toString(): String {
        return "${value}uc"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeUChar to IRCast(location, IRType(location, EcTypeUChar), IRInt(location, value))
    }
}