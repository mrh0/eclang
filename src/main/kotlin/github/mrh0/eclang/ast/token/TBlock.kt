package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRBlock
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.types.EcTypeNone

class TBlock(location: Loc, val statements: List<ITok>) : Tok(location) {
    override fun toString(): String {
        return "$statements"
    }

    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        cd.ctx().push()
        val ir = statements.map { it.process(cd, hint).second }
        val deferredIr = if (!cd.ctx().hasReturned()) cd.ctx().getNewDeferred().map { it.process(cd, hint).second } else listOf()
        val res = Pair(EcTypeNone, IRBlock(location, ir, deferredIr))
        cd.ctx().pop()
        return res
    }
}