package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.eclang.ir.data.IRCast
import github.mrh0.eclang.ir.data.IRChar
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeChar

class TCharNum(location: Loc, private val value: Int) : Tok(location) {
    override fun toString(): String {
        return "${value}c"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeChar to IRCast(location, IRType(location, EcTypeChar), IRInt(location, value))
    }
}