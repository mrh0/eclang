package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `:name`: exactly that atom. */
class TTypeAtom(location: Loc, val type: String) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val type: Type = cd.types.atom(cd.atoms.get(type.drop(1)).label)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeAtom"
    }
}
