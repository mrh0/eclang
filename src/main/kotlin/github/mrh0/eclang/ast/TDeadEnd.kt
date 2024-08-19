package github.mrh0.eclang.ast

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType

object TDeadEnd : Tok(Loc.IDENTITY) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        throw Exception("Processing dead end should not occur")
    }
}