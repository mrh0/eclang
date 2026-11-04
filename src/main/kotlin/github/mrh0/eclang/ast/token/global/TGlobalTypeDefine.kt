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

/** `declare type Name as T` and `type Name = T`: another name for T. With [distinct], `type Name = distinct T`: a new type with T's values. */
class TGlobalTypeDefine(location: Loc, val name: String, val type: ITok, private val distinct: Boolean = false) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        checkNewTypeName(cd, location, name)
        val target = type.process(cd, Types.None).first
        if (!distinct) cd.types.define(cd.module.name, name, target)
        else {
            val base = cd.storable(location, target, "the distinct type '$name'")
            cd.claimCName(location, cd.types.distinct(cd.module.name, name, base).cName, "the type '$name'", renamable = false)
        }
        return Types.None to IRPass(location, null)
    }

    override fun toString(): String {
        return "TGlobalTypeDefine($name)"
    }
}
