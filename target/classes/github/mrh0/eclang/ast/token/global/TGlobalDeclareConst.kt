package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.state.Constant
import github.mrh0.eclang.context.state.DeclaredConstant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.types.EcType
import github.mrh0.eclang.ir.IRGlobalDefine
import github.mrh0.eclang.ir.IRPass

class TGlobalDeclareConst(location: Loc, private val varName: String, private val type: ITok, private val externalName: String?) : Tok(location) {
    override fun process(cd: CompileData, hint: EcType): Pair<EcType, IIR> {
        val fixedName = testIdentifier(location, varName)
        val typeIr = type.process(cd, hint)
        cd.getGlobal().define(location, DeclaredConstant(fixedName, externalName ?: fixedName, typeIr.first))
        return Pair(typeIr.first, IRPass(location, "declare val $varName"))
    }

    override fun toString(): String {
        return "TGlobalDeclareConst($varName)"
    }
}