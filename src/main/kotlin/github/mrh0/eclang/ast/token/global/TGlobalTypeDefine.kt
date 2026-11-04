package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.ast.token.type.checkNewTypeName
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `declare type Name as T`: another name for T. */
class TGlobalTypeDefine(location: Loc, val name: String, val type: ITok) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        checkNewTypeName(cd, location, name)
        cd.types.define(cd.module.name, name, type.process(cd, Types.None).first)
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TGlobalTypeDefine($name)"
    }
}
