package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `T[]c`: a plain C array (a pointer to its first element). */
class TTypeCArray(location: Loc, val type: ITok) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type: Type = cd.types.cArray(type.process(cd, Types.None).first)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeCArray"
    }
}
