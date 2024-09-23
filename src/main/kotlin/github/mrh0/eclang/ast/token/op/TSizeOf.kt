package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRAddressOf
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.ir.data.IRSizeOf
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypePointer
import github.mrh0.eclang.types.numbers.signed.EcTypeLong

class TSizeOf(location: Loc, val type: ITok) : Tok(location) {
    override fun toString(): String {
        return "TSizeOf($type)"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val res = type.process(cd, hint)
        return EcTypeLong to IRSizeOf(location, res.second)
    }
}
