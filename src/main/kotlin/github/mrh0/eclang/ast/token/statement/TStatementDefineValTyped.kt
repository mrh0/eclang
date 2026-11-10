package github.mrh0.eclang.ast.token.statement

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.Lifetimes
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRStatementDefineVal
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TStatementDefineValTyped(location: Loc, private val varName: String, private val expr: ITok, private val type: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val (type, value) = defineValue(cd, location, varName, expr, type)
        val ivar = cd.ctx().define(location, Constant(fixedName, type, cd.localCName(fixedName)))
        Lifetimes.define(cd, fixedName, expr, type)
        return Types.None to IRStatementDefineVal(location, ivar, value)
    }

    override fun toString(): String {
        return "TStatementDefineValTyped($varName, $expr)"
    }
}
