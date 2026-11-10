package github.mrh0.eclang.ast.token.op

import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.function.call.Builtins
import github.mrh0.eclang.ast.token.statement.emptyValueProblem
import github.mrh0.eclang.ast.token.type.TTypeApplied
import github.mrh0.eclang.ast.token.type.refType
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.type.TTypeGeneric
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRRawCall
import github.mrh0.eclang.ir.data.IRNew
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * `new value`: a copy of value in the current pool, as a pointer (`new Point { x = 1 }` is a `@Point`). `new T` is
 * a pointer to an empty T, and `new T[n]` an array of n empty elements (a `T[]`). `new ... in p` uses pool p.
 * `new Pool` is a new pool, a child of the current one (or of p with `in p`), which `destroy` frees.
 */
class TNew(location: Loc, val value: ITok, val pool: ITok?, private val elementType: ITok? = null) : Tok(location) {
    override fun pointerFlow(): PointerFlow = if (copies) PointerFlow.From(value) else PointerFlow.Nothing

    /** Whether the last compiled `new` copied [value] (not `new T` or `new T[n]`); see Lifetimes. */
    var copies = true
        private set

    /** Whether the last compiled `new` made a pool (`new Pool`); see Lifetimes. */
    var makesPool = false
        private set

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> {
        val poolIR = Builtins.poolArgument(cd, location, pool, "`new`")
        val target = value.unnested()
        copies = false
        makesPool = false
        // `new (T)[n]`: [value] is the length.
        elementType?.let { return array(cd, it.process(cd, Types.None).first, value, poolIR).also { noteAllocation(cd) } }
        typeNamed(cd, target)?.let { t ->
            if (t !== cd.types.pool) return empty(cd, t, poolIR).also { noteAllocation(cd) }
            makesPool = true
            return t to IRRawCall(location, "__ec_pool_create", listOf(poolIR))
        }
        // In `new T[n]`, T is a type: a `ref rec` there is a pointer, as everywhere a type is written.
        if (target is TAccessor) typeNamed(cd, target.expr.unnested())?.let { return array(cd, refType(cd, it), target.index, poolIR).also { noteAllocation(cd) } }
        // `new @T[n]` reads as `@(T[n])`, but means an array of n pointers.
        if (target is TReadPointer) (target.expr.unnested() as? TAccessor)?.let { a ->
            typeNamed(cd, a.expr.unnested())?.let { return array(cd, cd.types.pointer(refType(cd, it)), a.index, poolIR).also { noteAllocation(cd) } }
        }
        (target as? TCreateRecordTyped)?.inNew = true
        copies = true
        val expected = (cd.types.nonNull(hint) as? Pointer)?.target ?: Types.None
        val v = cd.usable(location, value.process(cd, expected), "`new`")
        val type = cd.storable(location, v.first, "`new`")
        noteAllocation(cd)
        return cd.types.pointer(type) to IRNew(location, type, poolIR, cd.expect(location, v, type, "`new`"))
    }

    /** `new` without `in p` allocates in the current pool (see `CompileData.noteAllocation`). */
    private fun noteAllocation(cd: CompileData) {
        if (pool == null) cd.noteAllocation(location, "`new`")
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
        // `new (<T>)[n]` in a generic function is storage for elements to come, whatever T is: read only what was written.
        // T may be a pointer, with no empty value, so the compiler can't check that, and it needs `unsafe` whatever T is.
        val storage = !cd.genericContext.isNullOrEmpty() && (elementType is TTypeGeneric || elementType is TTypeApplied)
        if (storage && !cd.isUnsafe) throw EcError(location, "`new (<T>)[n]` leaves its elements unset, and T may be a pointer, which has no empty value: an element read before it is written would be a null that the type says can't be. Use `filled(n, value)` (core:array.ec) or a List, make the elements nullable (`new (<T>?)[n]`), or, when every element is written before it is read, say so with `unsafe new (<T>)[n]`.")
        if (!storage) emptyValueProblem(element)?.let { throw EcError(location, "`new $element[n]` fills the array with empty values, but $it. Build the array from values instead, e.g. `clone([a, b])`, or make the elements nullable: `new (${element}?)[n]`.") }
        if (count.unnested() is TRange) throw EcError(location, "`new $element[n]` takes a length, not a range.")
        val (t, ir) = cd.usable(location, count.process(cd, Types.Size), "the length in `new $element[n]`")
        if (!Ops.isInteger(t)) throw EcError(location, "The length in `new $element[n]` must be a whole number, but this is a $t.")
        if (t is IntLiteral && t.value < 0) throw EcError(location, "The length in `new $element[n]` can't be negative.")
        return type to IRNew(location, type, pool, null, ir)
    }

    /** The type [tok] names when it is a type name (`Point`, `geo.Point`, `@Point`) rather than a value, or null. */
    private fun typeNamed(cd: CompileData, tok: ITok): Type? = when {
        tok is TReadPointer -> typeNamed(cd, tok.expr.unnested())?.let { cd.types.pointer(it) }
        tok is TNamed && cd.findVar(Util.testIdentifier(location, tok.name)) == null && !TNamed.isCurrentPool(cd, tok.name) ->
            cd.types.lookup(cd.module.name, tok.name)
        tok is TAccessorNamed && tok.expr is TNamed && cd.findVar(tok.expr.name) == null ->
            cd.findModule(tok.expr.name)?.let { cd.types.lookupIn(it.name, tok.name) }
        else -> null
    }

    override fun toString() = "TNew($value${pool?.let { " in $it" } ?: ""})"
}
