package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.function.call.Builtins
import github.mrh0.eclang.ast.token.statement.emptyValueProblem
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.data.IRNew
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * `new value`: a copy of value in the current pool, as a pointer (`new Point { x = 1 }` is a `@Point`). `new T` is
 * a pointer to an empty T, and `new T[n]` an array of n empty elements (a `T[]`). `new ... in p` uses pool p.
 */
class TNew(location: Loc, val value: ITok, val pool: ITok?) : Tok(location) {
    /** Whether the last compiled `new` copied [value] (not `new T` or `new T[n]`); see Lifetimes. */
    var copies = true
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val poolIR = Builtins.poolArgument(cd, location, pool, "`new`")
        val target = value.unnested()
        copies = false
        typeNamed(cd, target)?.let { return empty(cd, it, poolIR) }
        if (target is TAccessor) typeNamed(cd, target.expr.unnested())?.let { return array(cd, it, target.index, poolIR) }
        copies = true
        val expected = (cd.types.nonNull(hint) as? Pointer)?.target ?: Types.None
        val v = cd.usable(location, value.process(cd, expected), "`new`")
        val type = cd.storable(location, v.first, "`new`")
        return cd.types.pointer(type) to IRNew(location, type, poolIR, cd.expect(location, v, type, "`new`"))
    }

    /** `new T`: a pointer to an empty [t]. */
    private fun empty(cd: CompileData, t: Type, pool: IIR): Pair<Type, IIR> {
        val type = cd.storable(location, t, "`new $t`")
        emptyValueProblem(type)?.let { throw EcError(location, "`new $t` makes an empty $t, but $it. Give it a value instead, e.g. `new $t { ... }`.") }
        return cd.types.pointer(type) to IRNew(location, type, pool, null)
    }

    /** `new T[n]`: an array of [count] empty [element]s. */
    private fun array(cd: CompileData, element: Type, count: ITok, pool: IIR): Pair<Type, IIR> {
        val type = cd.types.array(cd.storable(location, element, "the elements of `new $element[n]`"))
        emptyValueProblem(element)?.let { throw EcError(location, "`new $element[n]` fills the array with empty values, but $it. Build the array from values instead, e.g. `clone([a, b])`.") }
        if (count.unnested() is TRange) throw EcError(location, "`new $element[n]` takes a length, not a range.")
        val (t, ir) = cd.usable(location, count.process(cd, Types.Size), "the length in `new $element[n]`")
        if (!Ops.isInteger(t)) throw EcError(location, "The length in `new $element[n]` must be a whole number, but this is a $t.")
        if (t is IntLiteral && t.value < 0) throw EcError(location, "The length in `new $element[n]` can't be negative.")
        return type to IRNew(location, type, pool, null, ir)
    }

    /** The type [tok] names when it is a type name (`Point`, `geo.Point`) rather than a value, or null. */
    private fun typeNamed(cd: CompileData, tok: ITok): Type? = when {
        tok is TNamed && cd.findVar(Util.testIdentifier(location, tok.name)) == null && !TNamed.isCurrentPool(cd, tok.name) ->
            cd.types.lookup(cd.module.name, tok.name)
        tok is TAccessorNamed && tok.expr is TNamed && cd.findVar(tok.expr.name) == null ->
            cd.findModule(tok.expr.name)?.let { cd.types.lookupIn(it.name, tok.name) }
        else -> null
    }

    override fun toString() = "TNew($value${pool?.let { " in $it" } ?: ""})"
}
