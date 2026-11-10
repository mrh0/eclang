package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `@T`: a pointer to a T. */
class TTypePointerByName(location: Loc, val type: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type: Type = cd.types.pointer(type.process(cd, Types.None).first)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypePointerByName"
    }
}
