package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.DeclaredConstant
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `declare val NAME as T` / `declare var NAME as T`: a constant or variable defined in C. */
class TGlobalDeclareConst(location: Loc, private val varName: String, private val type: ITok, private val externalName: String?, private val assignable: Boolean = false) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val fixedName = testIdentifier(location, varName)
        val t = cd.storable(location, type.process(cd, Types.None).first, "declared value '$varName'")
        cd.reserveCName(externalName ?: fixedName)
        cd.getGlobal().define(location, DeclaredConstant(fixedName, externalName ?: fixedName, t, assignable))
        return Types.None to IRPass(location, "declare val $varName")
    }

    override fun toString(): String {
        return "TGlobalDeclareConst($varName)"
    }
}
