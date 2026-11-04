package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.context.state.CompileTimeConstant
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TStatementDefineConst(location: Loc, private val varName: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val (type, value) = constantValue(cd, location, varName, expr, null)
        cd.ctx().define(location, CompileTimeConstant(fixedName, type, fixedName, value, byName = false))
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TStatementDefineConst($varName, $expr)"
    }
}
