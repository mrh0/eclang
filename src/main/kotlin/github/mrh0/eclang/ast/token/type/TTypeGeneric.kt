package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError

import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.*

/** `<T>`: a generic type, decided by each call. `<mut T>` ([isMut]) takes only types whose fields can change (see Functions.instantiate). */
class TTypeGeneric(location: Loc, val name: String, val isMut: Boolean = false) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val signature = cd.mutTypeParams
        if (isMut && signature != null) signature.add(name)
        else if (isMut && cd.genericContext?.containsKey(name) != true) throw EcError(location, "`<mut $name>` says what a function's callers may give for $name, so it goes in the function's parameter or result types. Here, write `<$name>`.")
        val type: Type = cd.genericContext?.get(name) ?: cd.types.typeParam(name)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeGeneric"
    }
}
