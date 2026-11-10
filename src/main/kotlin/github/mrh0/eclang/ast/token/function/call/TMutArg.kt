package github.mrh0.eclang.ast.token.function.call

import github.mrh0.eclang.ast.IAssignable
import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.accessor.alignedAnywhere
import github.mrh0.eclang.ast.token.accessor.inPacked
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.CompileData
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.ir.IIR
import github.mrh0.eclang.ir.IRNamed
import github.mrh0.eclang.ir.data.IRAddressOf
import github.mrh0.eclang.ir.data.IRReadPointer
import github.mrh0.eclang.util.Util.testIdentifier
import github.mrh0.ect2.*

/**
 * `mut x`, an argument for a `mut` parameter: the address of x, a variable, field, element or `@p`, which the function may
 * change. Its type is `@T` for an x of type T; `Functions.score` only lets it go to a `mut` parameter of exactly that type.
 * The function can't keep the pointer (see `Lifetimes.place`), so what x may point to afterwards is all the call needs to
 * follow ([TExprCall]).
 */
class TMutArg(location: Loc, val expr: ITok) : Tok(location) {
    override fun pointerFlow(): PointerFlow = PointerFlow.From(expr)

    override fun process(cd: CompileData, hint: Type): Pair<Type, IIR> = MutPlace.address(cd, location, expr, "`mut ${MutPlace.shown(expr)}`")

    override fun toString() = "TMutArg($expr)"
}

/** A place a function is given to change: `mut x`, and the receiver of `x.f()` when f's first parameter is `mut`. */
internal object MutPlace {
    /** The address of the place [tok] as a `@T`, after checking it may change. [what] is how the code passes it, for errors. */
    fun address(cd: CompileData, location: Loc, tok: ITok, what: String): Pair<Type, IIR> {
        val t = tok.unnested()
        if (t is TNamed) {
            if (TNamed.isCurrentPool(cd, t.name)) throw EcError(location, "$what would let the function change `pool`, the current pool, which can't be assigned. To allocate in another pool for a while, use a `pool do` block.")
            val name = testIdentifier(t.location, t.name)
            val found = cd.getVar(t.location, name)
            val v = (found as? NarrowedVar)?.base ?: found
            if (!v.canAssign()) throw EcError(location, "$what lets the function change '${t.name}', but '${t.name}' is ${v.kind()}, which can't change. Declare it with `var` to pass it this way.")
            github.mrh0.eclang.context.Lifetimes.checkAlive(cd, t.location, name, t.name)
            // The function may set it to anything of its declared type, so a check made before the call no longer holds.
            cd.ctx().forget(name)
            cd.changedByCalls += name
            return cd.types.pointer(v.getType()) to IRAddressOf(location, IRNamed(location, v.cName()))
        }
        if (t !is IAssignable || (t is TAccessor && t.index.unnested() is TRange))
            throw EcError(location, "$what passes a variable for the function to change, but this is a value, which has no place to change. Store it in a `var` first (`var x = ...`), then pass `mut x`.")
        val place = t.place(cd)
        place.readOnly?.let { throw EcError(location, "Can't pass this as $what: $it") }
        root(t)?.let { cd.changedByCalls += testIdentifier(it.location, it.name) }
        // `mut p` of a `mut` parameter p: what it points to may change.
        place.name?.let { cd.ctx().forget(it) }
        if (t is TAccessorNamed && t.bitFieldOf != null)
            throw EcError(location, "Can't pass bit-field '${t.name}' as $what: it doesn't start at a whole byte, so there is no pointer to it. Copy it into a `var`, pass that, and copy it back.")
        if (inPacked(t) && !alignedAnywhere(place.type))
            throw EcError(location, "Can't pass this ${place.type} as $what: it is inside a `packed` record, so it may be at any address, where C can't use a pointer to it. Copy it into a `var`, pass that, and copy it back.")
        // `mut p` of a `mut` parameter p passes p itself, not `&*p`.
        val ir = place.ir.let { if (it is IRReadPointer) it.expr else IRAddressOf(location, it) }
        return cd.types.pointer(place.type) to ir
    }

    /**
     * The type of the place [tok] (a variable, field, element or `@p`), worked out from the declarations alone, without compiling
     * it, or null when it isn't such a place or that takes compiling. A method call uses it to decide whether its receiver goes
     * by address, before compiling the receiver once, either way.
     */
    fun typeOf(cd: CompileData, tok: ITok): Type? = when (val t = tok.unnested()) {
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) null else cd.findVar(testIdentifier(t.location, t.name))?.let { ((it as? NarrowedVar)?.base ?: it).getType() }
        is TAccessorNamed -> typeOf(cd, t.expr)?.let { base ->
            val record = (if (base is Pointer) base.target else base) as? Record
            record?.takeIf { !it.isExternal }?.field(t.fixedName)
        }
        is TAccessor -> if (t.index.unnested() is TRange) null else typeOf(cd, t.expr)?.let { base ->
            when (base) {
                is ArrayType -> base.element.takeIf { !base.isC }
                is FixedArray -> base.element
                else -> null
            }
        }
        is TReadPointer -> (typeOf(cd, t.expr) as? Pointer)?.target
        else -> null
    }

    /** The variable [tok] is a part of (x for `x.a[i].b`), or null when it is reached through a pointer. */
    private fun root(tok: ITok): TNamed? = when (val t = tok.unnested()) {
        is TNamed -> t
        is TAccessorNamed -> if (t.throughPointer) null else root(t.expr)
        is TAccessor -> if (t.inValue) root(t.expr) else null
        else -> null
    }

    /** How [tok] is written, for errors: `x`, `x.a`. */
    fun shown(tok: ITok): String = when (val t = tok.unnested()) {
        is TNamed -> t.name
        is TAccessorNamed -> "${shown(t.expr)}.${t.name}"
        is TReadPointer -> if (t.mutParameter) shown(t.expr) else "@${shown(t.expr)}"
        else -> "x"
    }

    /**
     * The variable and fields [tok] names, as `x.a.b`, for the rule that one call can't be given the same variable twice to
     * change; null when it can't tell (an element, or through a pointer other than a `mut` parameter).
     */
    fun path(tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> t.name
        is TAccessorNamed -> if (t.throughPointer) null else path(t.expr)?.let { "$it.${t.name}" }
        is TReadPointer -> if (t.mutParameter) path(t.expr) else null
        else -> null
    }
}
