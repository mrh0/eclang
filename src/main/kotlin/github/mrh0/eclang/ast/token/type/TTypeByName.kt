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

/** `Int`, `Point`, ...: a type by name. */
class TTypeByName(location: Loc, val type: String) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        // `T` inside `rec List<T>` is its type parameter.
        val found: Type = cd.genericContext?.get(type) ?: cd.lookupType(location, type)
        if (found is GenericRecord) throw EcError(location, "${cd.types.storageProblem(found)}.")
        val type = refType(cd, found)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeByName"
    }
}
