package github.mrh0.eclang.ast.token.global

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ast.token.type.checkNewTypeName
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRPass
import github.mrh0.ect2.FixedArray
import github.mrh0.ect2.FunctionType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `declare type Name as T` ([external]) and `type Name = T`: another name for T. With [distinct], `type Name = distinct T`: a new type with T's values. */
class TGlobalTypeDefine(location: Loc, val name: String, val type: ITok, private val distinct: Boolean = false, private val external: Boolean = false) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        checkNewTypeName(cd, location, name)
        // `declare type` names a C type, so a function type in it is a C function pointer.
        val target = type.process(cd, Types.None).first.let { if (external) cd.types.inC(it) else it }
        if (external) (target as? FunctionType)?.params?.firstOrNull { it is FixedArray }?.let {
            throw EcError(location, "C can't take an array by value: a C function given a $it gets a pointer. Write the parameter as a pointer to the elements, e.g. `@${(it as FixedArray).element}`.")
        }
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
