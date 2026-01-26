package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.numbers.signed.EcTypeInt

class TInteger(location: Loc, private val value: Int) : Tok(location) {
    override fun toString(): String {
        return "${value}i"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeInt to IRInt(location, value)
    }
}