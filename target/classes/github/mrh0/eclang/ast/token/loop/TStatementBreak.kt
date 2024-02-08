package github.mrh0.eclang.ast.token.loop

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.loop.IRBreak
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone

class TStatementBreak (location: Loc) : Tok(location) {
    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        return Pair(EcTypeNone, IRBreak(location))
    }

    override fun toString(): String {
        return "TBreak"
    }
}
