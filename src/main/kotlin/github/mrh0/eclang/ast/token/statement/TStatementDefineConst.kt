package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.ir.IRStatementDefineConst

class TStatementDefineConst(location: Loc, private val varName: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, varName)
        val ir = expr.process(cd, hint)
        val index = cd.ctx().define(location, Constant(fixedName, ir.first))
        return Pair(ir.first, IRStatementDefineConst(location, index, ir.second))
    }

    override fun toString(): String {
        return "TDefineConst($expr)"
    }
}