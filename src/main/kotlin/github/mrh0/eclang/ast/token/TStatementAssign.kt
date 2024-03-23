package github.mrh0.eclang.ast.token

import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementAssign
import github.mrh0.eclang.types.EcType

class TStatementAssign(location: Loc, private val varName: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData): Pair<EcType, IIR> {
        val ir = expr.process(cd)
        val index = cd.ctx().assign(location, varName, ir.first)
        return Pair(ir.first, IRStatementAssign(location, index, ir.second))
    }

    override fun toString(): String {
        return "TAssign($expr)"
    }
}