package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.context.state.Variable
import github.mrh0.eclang.ir.IRGlobalDefine

class TGlobalDefine(location: Loc, private val varName: String, private val expr: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, varName)
        val ir = expr.process(cd, hint)
        val ivar = cd.getGlobal().define(location, Variable(fixedName, ir.first))
        return Pair(ir.first, IRGlobalDefine(location, ivar, ir.second))
    }

    override fun toString(): String {
        return "TGlobalDefine($expr)"
    }
}