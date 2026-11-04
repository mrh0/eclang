package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementDefineVar
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TStatementDefineVar(location: Loc, private val varName: String, private val expr: ITok, private val volatile: Boolean = false) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val (type, value) = defineValue(cd, location, varName, expr, null)
        val ivar = cd.ctx().define(location, Variable(fixedName, type, cd.localCName(fixedName)))
        Lifetimes.define(cd, fixedName, expr, type)
        return Types.None to IRStatementDefineVar(location, ivar, value, volatile)
    }

    override fun toString(): String {
        return "TStatementDefineVar($varName, $expr)"
    }
}
