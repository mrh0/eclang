package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.statement.defineValue
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRGlobalDefineConst
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

class TGlobalDefineConstTyped(location: Loc, private val varName: String, private val expr: ITok, private val type: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val (varType, value) = defineValue(cd, location, varName, expr, type)
        val ivar = cd.getGlobal().define(location, Constant(fixedName, varType, cd.claimCName(location, cd.module.cName(fixedName), "global '$varName'")))
        return Types.None to IRGlobalDefineConst(location, ivar, value)
    }

    override fun toString(): String {
        return "TGlobalDefineConstTyped($varName, $expr)"
    }
}
