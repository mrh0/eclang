package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.statement.defineValue
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRGlobalDefine
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TGlobalDefineTyped(location: Loc, private val varName: String, private val expr: ITok, private val type: ITok, private val volatile: Boolean = false) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val (varType, value) = defineValue(cd, location, varName, expr, type)
        val ivar = cd.getGlobal().define(location, Variable(fixedName, varType, cd.claimCName(location, cd.module.cName(fixedName), "global '$varName'")))
        return Types.None to IRGlobalDefine(location, ivar, value, volatile)
    }

    override fun toString(): String {
        return "TGlobalDefineTyped($varName, $expr)"
    }
}
