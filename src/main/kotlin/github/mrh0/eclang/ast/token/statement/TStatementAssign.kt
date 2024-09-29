package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.ast.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.error.EcAssignConstError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementAssign
import github.mrh0.eclang.types.EcType

class TStatementAssign(location: Loc, private val varName: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, varName)
        val ir = expr.process(cd, hint)
        val ivar = cd.ctx().assign(location, fixedName, ir.first)
        if (!ivar.canAssign()) throw EcAssignConstError(location, varName)
        return Pair(ir.first, IRStatementAssign(location, ivar, ir.second))
    }

    override fun toString(): String {
        return "TAssign($expr)"
    }
}