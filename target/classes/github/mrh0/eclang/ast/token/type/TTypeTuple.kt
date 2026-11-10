package github.mrh0.eclang.ast.token.type

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.NonValueTok
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRType
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types

/** `(A, B)`: a tuple type, a struct in C. */
class TTypeTuple(location: Loc, val types: List<ITok>) : NonValueTok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val parts = types.map { it.process(cd, Types.None).first }
        if (Types.None in parts) throw EcError(location, "None has no values, so it can't be a part of a tuple. Leave that part out.")
        // A tuple is a C struct, so unlike a parameter its parts need a C type, except a `<T>`, which a call replaces.
        parts.filterNot { cd.types.hasTypeParam(it) }.firstNotNullOfOrNull { cd.types.storageProblem(it) }?.let { throw EcError(location, "A tuple can't hold this: $it.") }
        val type = cd.types.tuple(parts)
        return type to IRType(location, type)
    }

    override fun toString(): String {
        return "TTypeTuple($types)"
    }
}
