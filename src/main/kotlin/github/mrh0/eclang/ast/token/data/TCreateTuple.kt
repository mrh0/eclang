package github.mrh0.eclang.ast.token.data

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.ArgumentOrder
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRCreateTuple
import github.mrh0.ect2.AtomLiteral
import github.mrh0.ect2.Tuple
import github.mrh0.ect2.Type
import github.mrh0.ect2.Types
import github.mrh0.ect2.Union

/**
 * `(1, "one")`: a tuple. With an expected tuple type the parts must fit its part types; otherwise each part takes the
 * type it would have in a variable, except that an atom is an Atom, so that `(:ok, 1)` fits `(Atom, Int)`.
 * Also the left side of `(a, b) = ...` and, built by the Visitor, of `val (a, b) = ...` (see `TDestructure`).
 */
class TCreateTuple(location: Loc, val values: List<ITok>) : Tok(location) {
    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val expected = expected(cd, hint)?.elements
        val order = ArgumentOrder(cd)
        val processed = order.inOrder(location, values) { i, value -> value.process(cd, expected?.get(i) ?: Types.None) }
        val parts = expected ?: processed.mapIndexed { i, value -> stored(cd, value, i) }
        val tuple = cd.types.tuple(parts)
        return tuple to order.wrap(location, IRCreateTuple(location, tuple, processed.mapIndexed { i, value -> cd.expect(location, value, parts[i], "part ${i + 1} of the tuple") }))
    }

    /** The tuple type of the right size in [hint], which may be a union such as `(Int, Int)?`. */
    private fun expected(cd: CompileData, hint: Type): Tuple? {
        val candidates = if (hint is Union) hint.members else listOf(hint)
        return candidates.filterIsInstance<Tuple>().singleOrNull { it.elements.size == values.size }?.takeUnless { cd.types.hasTypeParam(it) }
    }

    private fun stored(cd: CompileData, value: Pair<Type, IIR>, index: Int): Type {
        val what = "part ${index + 1} of the tuple"
        val type = cd.usable(location, value, what).first
        if (type === Types.Null) throw EcError(location, "Can't tell the type of $what from `null`. Write the tuple's type: `val t: (T?, ...) = (null, ...)`.")
        return if (type is AtomLiteral) Types.Atom else cd.storable(location, type, what)
    }

    /** The source text of a pattern of names, such as `(a, (b, _))`, for errors. */
    fun pattern(): String = values.joinToString(", ", "(", ")") {
        when (val t = it.unnested()) {
            is TNamed -> t.name
            is TCreateTuple -> t.pattern()
            else -> "..."
        }
    }

    override fun toString(): String {
        return "TCreateTuple($values)"
    }
}
