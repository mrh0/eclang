package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.types.EcTypeAny
import github.mrh0.eclang.types.EcTypeNone

class TBlock(location: Loc, val statements: MutableList<ITok>) : Tok(location) {
    override fun toString(): String {
        return "$statements"
    }

    override fun process(cd: CompileData): Pair<EcTypeAny, IIR> {
        val ir = statements.map { it.process(cd).second }
        return Pair(EcTypeNone, IRBlock(location, ir))
    }
}