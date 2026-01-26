package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRInt
import github.mrh0.eclang.ir.data.IRLong
import github.mrh0.eclang.ir.data.IRNull
import github.mrh0.eclang.ir.data.IRShort
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNull
import github.mrh0.eclang.types.numbers.signed.EcTypeInt
import github.mrh0.eclang.types.numbers.signed.EcTypeLong
import github.mrh0.eclang.types.numbers.signed.EcTypeShort

class TNull(location: Loc) : Tok(location) {
    override fun toString(): String {
        return "Null"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        return EcTypeNull to IRNull(location)
    }
}