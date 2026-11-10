package github.mrh0.eclang.context

import github.mrh0.eclang.ast.token.data.TNull

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.ast.PointerFlow
import github.mrh0.eclang.ast.Tok
import github.mrh0.eclang.ast.token.TNamed
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.branch.TWhenExpr
import github.mrh0.eclang.ast.token.data.record.TUpdateRecord
import github.mrh0.eclang.ast.token.function.call.TSafeCall
import github.mrh0.eclang.ast.token.op.TNullishJump
import github.mrh0.eclang.ast.token.data.TCreateArray
import github.mrh0.eclang.ast.token.data.TCreateCArray
import github.mrh0.eclang.ast.token.data.TCreateTuple
import github.mrh0.eclang.ast.token.data.TInterpolated
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.data.record.RecordDefaults
import github.mrh0.eclang.ast.token.function.TLambda
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.call.TEntryPool
import github.mrh0.eclang.ast.token.op.TAddressOf
import github.mrh0.eclang.ast.token.op.TCastNotNull
import github.mrh0.eclang.ast.token.op.TNew
import github.mrh0.eclang.ast.token.op.TNullishCoalescing
import github.mrh0.eclang.ast.token.op.TPipe
import github.mrh0.eclang.ast.token.op.TRange
import github.mrh0.eclang.ast.token.op.TIt
import github.mrh0.eclang.ast.token.op.TCallValue
import github.mrh0.eclang.ast.token.op.TReadPointer
import github.mrh0.eclang.ast.token.op.TTry
import github.mrh0.eclang.ast.token.type.TCast
import github.mrh0.eclang.ast.token.type.TCastUnsafe
import github.mrh0.eclang.ast.unnested
import github.mrh0.eclang.context.function.Overload
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcMemoryError
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * Memory that is freed before the function returns: a local variable, the elements of an array literal (C stack
 * memory), or the pool of a `pool do` block. It is freed when the scope at [depth] ends (see `ContextStack.depth`),
 * so a pointer into it may only be kept in variables defined at that depth or deeper.
 * [memory] names it in errors ("'x', a local variable"), [ends] says when it is freed, [fix] how to keep the value.
 * [holder]: it is the storage of an array, so an element read from it doesn't point into it; [inside] is the memory the
 * elements may point into. [pool]: the pool of a `pool do` block (see [ContextStack.poolOf]). [loopPools]: it is memory
 * a round of these loops allocates, which their own pools would free (only while [ContextStack.imagining], see [LoopPools]).
 */
class Local(val depth: Int, val memory: String, val ends: String, val fix: String, val holder: Boolean = false, val inside: Local? = null, val pool: String? = null,
            val loopPools: List<LoopPools.Loop> = listOf()) {
    /** The memory a value read out of this memory may point into. */
    fun stored(): Local? = if (holder) inside else this

    fun holding(inside: Local?) = Local(depth, memory, ends, fix, holder, inside, pool, loopPools)

    /** How to keep a value, written [shown] (null: not a name), that points here: `keep(x)` for memory of a `pool do` block. */
    fun fixFor(shown: String?): String = if (pool == null) fix else
        "Keep a copy instead: ${if (shown != null) "`keep($shown)`" else "wrap the value in `keep(...)`, which"} copies it into the pool the function was called with, which outlives its `pool` blocks."
}

/**
 * Catches dangling pointers at compile time: a pointer to memory that is freed when a block or the function ends
 * must not be returned, thrown, or stored where it outlives that memory. Values are followed through variables
 * (`val p = addrof x; ret p`), record and array literals, `new`, pipes, calls (a call's result may point into its
 * arguments, or into the pools it is given), and what is read out of memory: `p.next`, `a[i]`, `a[x..<y]` and `@p`
 * may point where p and a do, since what is stored there must live at least as long as p and a's memory.
 *
 * Memory of a pool that isn't tied to a block (`new Pool`) is followed the same way by [allocations]: after
 * `clearPool(p)` or `destroy p`, a variable holding something allocated in p before can't be used. Pools have an
 * identity of their own ([poolId]), so that `val q = p` and a field `h.p` are the same pool, and freeing a pool frees its children.
 *
 * Code is compiled once, in source order, but a loop's body runs again with what the previous round left: see [followed]
 * for local memory, and [LoopRound] for pools.
 */
object Lifetimes {
    /** The current pool outside `pool do` blocks: it belongs to the caller, so it holds what the caller holds. */
    const val CALLER_POOL = "#caller"

    /** In [sources]: memory allocated in the caller's pool, by `new`, `${...}` or a call (see [Escapes]). */
    const val POOL_SOURCE = "#pool"

    /** In [sources]: a global, which outlives every call (see [Escapes]). */
    const val GLOBAL_SOURCE = "#global"

    /** Starts the key of what a place behind a pointer parameter holds, in [ContextStack.Scope.allocations] (`@o.name`). */
    private const val BEHIND_PARAMETER = "@"

    /** The local memory the value of [tok] points into, or null when it lives at least as long as the function call. */
    fun value(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TAddressOf -> place(cd, t.expr)
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) currentPoolMemory(cd) else cd.ctx().stack.originOf(Util.testIdentifier(t.location, t.name))
        is TCreateArray -> deepest(listOf(if (t.isFixed) null else arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TCreateCArray -> deepest(listOf(arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TAccessorNamed -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null) + flows(t).map { value(cd, it) })
        // An element is what is stored in the array, unlike a slice, which still points into it.
        is TAccessor -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null, value(cd, t.expr).let { if (t.index.unnested() is TRange) it else it?.stored() }))
        is TNew -> deepest(listOf(pool(cd, t.pool)) + flows(t).map { value(cd, it) })
        is TExprCall -> deepest(t.pools.map { pool(cd, it) } + flows(t).map { value(cd, it) })
        is TInterpolated -> currentPoolMemory(cd)
        is TCreateRecord -> record(cd, t, t.defaults)
        is TCreateRecordTyped -> record(cd, t, t.defaults)
        is TPipe -> piped(cd, t) { value(cd, it) }
        is TIt -> unpiped(cd) { value(cd, it) }
        is TLambda -> deepest(listOf(if (t.allocates) currentPoolMemory(cd) else null) + t.captured.map { c -> value(cd, c)?.let { shared(it, t.shared[c]) } })
        else -> deepest(flows(t).map { value(cd, it) })
    }

    /** [local], the memory of the `var` [name] a lambda shares (if it is one), as errors about that lambda name it. */
    private fun shared(local: Local, name: String?): Local = if (name == null) local else
        Local(local.depth, "'$name', a `var` that the lambda uses (a lambda shares the `var`s it uses, it doesn't copy them)", local.ends,
            "To give the lambda a copy instead, copy the value into a `val` before the lambda (`val ${name}Now = $name`) and use that in it.",
            local.holder, local.inside, local.pool, local.loopPools)

    /**
     * What the value of [tok] may point into, in terms of the function being compiled, for [Escapes]: the names of its
     * parameters, [POOL_SOURCE] for its caller's pool and [GLOBAL_SOURCE] for globals. It follows values as [value] does,
     * so it includes what the memory a pointer points at holds; `p[]` stands for what the elements of p point into.
     */
    fun sources(cd: CompileData, tok: ITok): Set<String> = when (val t = tok.unnested()) {
        is TAddressOf -> placeSources(cd, t.expr)
        is TNamed -> namedSources(cd, t, pointees = false)
        is TAccessorNamed -> (if (t.viewsValue) placeSources(cd, t.expr) else setOf()) + flowSources(cd, t)
        is TAccessor -> (if (t.viewsValue) placeSources(cd, t.expr) else setOf()) +
            sources(cd, t.expr).let { if (t.index.unnested() is TRange) it else it.map(Escapes::element).toSet() }
        is TNew -> poolSources(cd, t.pool) + flowSources(cd, t)
        is TExprCall -> t.pools.flatMap { poolSources(cd, it) }.toSet() + flowSources(cd, t)
        is TEntryPool -> setOf(POOL_SOURCE)
        is TInterpolated -> poolSources(cd, null)
        is TCreateRecord -> (if (t.defaults.allocates) poolSources(cd, null) else setOf()) + flowSources(cd, t)
        is TCreateRecordTyped -> (if (t.defaults.allocates) poolSources(cd, null) else setOf()) + flowSources(cd, t)
        is TPipe -> piped(cd, t) { sources(cd, it) }
        is TIt -> unpiped(cd) { sources(cd, it) } ?: setOf()
        is TLambda -> (if (t.allocates) poolSources(cd, null) else setOf()) + flowSources(cd, t)
        else -> flowSources(cd, t)
    }

    /**
     * Like [sources], but only the memory the pointer [tok] points at, not what that memory holds: `new Reading { c }` is
     * in the current pool, and a store through it doesn't write into what c points to. What was stored in a field or an
     * element may be anything [sources] says. A call's result may point into its arguments, as for reads ([sources]): a store
     * through `find(m, k).value = v` may write into what m points to.
     */
    fun pointeeSources(cd: CompileData, tok: ITok): Set<String> = when (val t = tok.unnested()) {
        is TAddressOf -> placeSources(cd, t.expr)
        is TNamed -> namedSources(cd, t, pointees = true)
        is TNew -> poolSources(cd, t.pool)
        is TInterpolated -> poolSources(cd, null)
        is TAccessorNamed, is TAccessor, is TReadPointer -> sources(cd, t)
        is TExprCall -> t.storedInto.flatMap { sources(cd, it) }.toSet()
        is TCallValue -> t.storedInto.flatMap { sources(cd, it) }.toSet()
        is TPipe -> piped(cd, t) { pointeeSources(cd, it) }
        is TIt -> unpiped(cd) { pointeeSources(cd, it) } ?: setOf()
        is TLambda -> if (t.allocates) poolSources(cd, null) else setOf()
        else -> if (isLiteral(t)) setOf() else flows(t).flatMap { pointeeSources(cd, it) }.toSet()
    }

    private fun flowSources(cd: CompileData, t: ITok): Set<String> = flows(t).flatMap { sources(cd, it) }.toSet()

    /**
     * Whose stores [Escapes] follows in the code being compiled, and in terms of what: a function's body and its parameters, or a
     * lambda and [Closure.escapeParams]. Null for the value of a global.
     */
    private fun escapeScope(cd: CompileData): Pair<Any, Set<String>>? =
        cd.closure?.let { it to it.escapeParams } ?: cd.function?.let { o -> o.body!! to o.params.map { it.name }.toSet() }

    /** What a lambda uses of the function around it, named [name], stands for in [Escapes] ([value]: the value of a `var`, not its place); null if it isn't one. */
    private fun captureSource(cd: CompileData, name: String, value: Boolean): String? {
        val capture = cd.closure?.captures?.get(name) ?: return null
        return if (capture.byReference && value) Escapes.captureValue(name) else if (capture.byReference || value) Escapes.capture(name) else null
    }

    /** What the variable [t] may point into ([pointees]: point at): a parameter itself, what a local was given, or a global. */
    private fun namedSources(cd: CompileData, t: TNamed, pointees: Boolean): Set<String> {
        if (TNamed.isCurrentPool(cd, t.name)) return poolSources(cd, null)
        val name = Util.testIdentifier(t.location, t.name)
        val v = cd.ctx().getRaw(name) ?: return setOfNotNull(captureSource(cd, name, value = true) ?: GLOBAL_SOURCE)
        val own = if (v.kind() == "a parameter" && escapeScope(cd)?.second?.contains(t.name) == true) setOf(t.name) else setOf()
        return own + if (pointees) cd.escapes.localPointees(name) else cd.escapes.localSources(name)
    }

    /** What memory allocated in the pool [tok] (null: the current pool) is part of; nothing for a `pool do` block's own pool. */
    private fun poolSources(cd: CompileData, tok: ITok?): Set<String> = when {
        tok != null -> sources(cd, tok)
        cd.poolOrigin() == null -> setOf(POOL_SOURCE)
        else -> setOf()
    }

    /** What the memory of the place [tok] (what `addrof` points at, or what an assignment writes) is part of, as [sources] says. */
    private fun placeSources(cd: CompileData, tok: ITok): Set<String> = when (val t = tok.unnested()) {
        is TNamed -> Util.testIdentifier(t.location, t.name).let { name ->
            if (cd.ctx().getRaw(name) != null) setOf() else setOf(captureSource(cd, name, value = false) ?: GLOBAL_SOURCE)
        }
        is TCastNotNull -> placeSources(cd, t.expr)
        is TAccessorNamed -> if (t.throughPointer) pointeeSources(cd, t.expr) else placeSources(cd, t.expr)
        is TAccessor -> if (t.inValue) placeSources(cd, t.expr) else pointeeSources(cd, t.expr)
        is TReadPointer -> pointeeSources(cd, t.expr)
        else -> setOf()
    }

    /** After `val name = value` or `name = value`: what the local [name] may point into, for [Escapes]. With [element], it is an element of [value]. */
    private fun noteLocal(cd: CompileData, name: String, value: ITok, element: Boolean) {
        val from = sources(cd, value).let { if (element) it.map(Escapes::element).toSet() else it }
        cd.escapes.addLocal(name, from, if (element) from else pointeeSources(cd, value))
    }

    /**
     * After `target = value` (of [type]) in a function: a local now also points where [value] does, and a store through a
     * parameter or into a global is something the function keeps, which its calls must check ([Escapes]).
     */
    private fun noteStore(cd: CompileData, target: ITok, value: ITok, type: Type) {
        if (!holdsPointer(type)) return
        val root = localRoot(cd, target)
        if (root != null) {
            if (target.unnested() is TNamed) noteLocal(cd, root, value, element = false)
            else cd.escapes.addLocal(root, sources(cd, value), setOf())
            return
        }
        val (key, params) = escapeScope(cd) ?: return
        val behind = placeSources(cd, target).filter { Escapes.base(it) in params || it == GLOBAL_SOURCE }
        for (j in sources(cd, value)) if (Escapes.base(j) in params || j == POOL_SOURCE) for (k in behind) cd.escapes.store(key, Escapes.Store(j, k))
    }

    /**
     * A call of [callee] (named [name]) given [args]: for each, the parameter it is for, the value, its type, and whether it
     * is `mut`. Recorded for [Escapes.check], which knows what the callee keeps once every body is compiled.
     */
    fun noteCall(cd: CompileData, location: Loc, name: String, callee: Overload, args: List<Triple<String, ITok, Boolean>>, types: List<Type>) {
        // A C function is followed only for what it keeps (`kept`), with what it stores recorded under its overload.
        val body: Any = callee.body ?: callee.takeIf { o -> o.params.any { it.isKept } } ?: return
        val caller = escapeScope(cd)
        followed(cd) {
            val given = args.mapIndexed { i, (param, tok, mut) ->
                val pointer = holdsPointer(types[i])
                Escapes.Arg(param, if (pointer) value(cd, tok) else null, pointer, sources(cd, tok), pointeeSources(cd, tok), !mut, spelled(tok))
            }
            cd.escapes.call(Escapes.Call(location, name, caller?.first, caller?.second.orEmpty(), body, given,
                currentPoolMemory(cd), poolSources(cd, null), cd.isUnsafe), imagined = cd.ctx().stack.imagining)
        }
    }

    /**
     * The lambda [closure], just compiled at [location] ([unsafe]: inside `unsafe`): what it keeps of what it uses is checked as if it
     * were called here, where that memory is known, like a call given each capture (see [Escapes]). What it keeps of its own parameters,
     * or of memory it allocates where it is called, can't be checked anywhere: [Escapes.check] rejects that.
     */
    fun noteLambda(cd: CompileData, location: Loc, closure: Closure, type: FunctionType, unsafe: Boolean) {
        cd.escapes.lambda(closure, type, unsafe)
        val caller = escapeScope(cd)
        followed(cd) {
            val given = closure.captures.values.flatMap { c ->
                val read = TNamed(location, c.name)
                val value = Escapes.Arg(if (c.byReference) Escapes.captureValue(c.name) else Escapes.capture(c.name), if (holdsPointer(c.type)) value(cd, read) else null,
                    holdsPointer(c.type), sources(cd, read), pointeeSources(cd, read), true, c.name)
                if (!c.byReference) listOf(value)
                else listOf(value, Escapes.Arg(Escapes.capture(c.name), value(cd, c.token), true, sources(cd, c.token), pointeeSources(cd, c.token), true, "`addrof ${c.name}`"))
            }
            cd.escapes.call(Escapes.Call(location, "the lambda", caller?.first, caller?.second.orEmpty(), closure, given,
                currentPoolMemory(cd), poolSources(cd, null), unsafe, lambda = closure), imagined = cd.ctx().stack.imagining)
        }
    }

    /**
     * A call at [location] of a function value of [type] (written [what]) given [args]: checked, once every body is compiled, against
     * what the functions and lambdas of that type keep in their arguments ([Escapes]).
     */
    fun noteValueCall(cd: CompileData, location: Loc, what: String, type: FunctionType, args: List<ITok>) {
        val caller = escapeScope(cd)
        followed(cd) {
            val given = args.mapIndexed { i, tok ->
                val pointer = holdsPointer(type.params[i])
                Escapes.Arg(Escapes.position(i), if (pointer) value(cd, tok) else null, pointer, sources(cd, tok), pointeeSources(cd, tok), true, spelled(tok))
            }
            cd.escapes.call(Escapes.Call(location, what, caller?.first, caller?.second.orEmpty(), type, given,
                currentPoolMemory(cd), poolSources(cd, null), cd.isUnsafe), imagined = cd.ctx().stack.imagining)
        }
    }

    /**
     * The argument [value] (of [type]) of the `escaping` parameter [param] of [name]: the callee keeps it after the call, so it
     * must not be in a pool this function made, which it may free while the callee still uses it. With [followed] the callee is
     * an eclang function, whose call [Escapes] checks for local memory; a C function's is checked here.
     */
    fun checkEscaping(cd: CompileData, location: Loc, name: String, param: String, value: ITok, type: Type, followed: Boolean) {
        if (!holdsPointer(type)) return
        // `unsafe` vouches for the code as written, not for a pool the compiler would give a loop's rounds (see LoopPools).
        if (cd.isUnsafe) return followed(cd) { value(cd, value)?.let { LoopPools.keep(it, null) } }
        val keeps = "'$name' keeps '$param' after it returns (it is `escaping`: another thread or C code uses it)"
        if (!followed) followed(cd) {
            value(cd, value)?.takeIf { !LoopPools.keep(it, null) }?.let { local ->
                throw EcMemoryError(location, "$keeps, but here it points to ${local.memory}, which is freed ${local.ends}. ${local.fix}")
            }
        }
        val stack = cd.ctx().stack
        allocations(cd, value).firstOrNull { it.pool in stack.ownedPools }?.let { alloc ->
            throw EcMemoryError(location, "$keeps, but here it points to memory of pool '${alloc.poolName}', which this function made and may free while it is still used. " +
                "Allocate it in a pool that lives as long as the program, or the thread: the current pool of `main`, or `new ... in p` with a p that is never destroyed.")
        }
    }

    /**
     * The pool [value] given to the `own` parameter [param] of [name] at [location]: the callee takes it over and may free it, so it
     * must be a pool this function made (or took over), and counts as freed after the call: it, and what is in it, can't be used.
     */
    fun giveAway(cd: CompileData, location: Loc, name: String, param: String, value: ITok) {
        val shown = shown(value)
        if (!cd.isUnsafe && !owns(cd, value, clear = false))
            throw EcMemoryError(location, "'$name' takes over the pool '$param' (`own`) and may free it, so it must be a pool this function made with `new Pool`, but ${notOwned(cd, value)}. " +
                "Make one here (`val p = new Pool`), take it over too (`own $shown: Pool`), or, when nothing else uses what is in it, say so with `unsafe $name(...)`.")
        kill(cd, location, value, free = true, how = "`$name(...)` at line ${location.getLine()}, which took the pool over (`own`),")
    }

    /** The value of the record literal [t]: its fields, and what the defaults it used ([defaults]) point into. */
    private fun record(cd: CompileData, t: ITok, defaults: RecordDefaults): Local? =
        deepest(listOf(if (defaults.allocates) currentPoolMemory(cd) else null, if (defaults.onStack) arrayLiteral(cd) else null) + flows(t).map { value(cd, it) })

    /**
     * The values whose memory the value of [t] may point into, besides memory [t] allocates or is itself: the parts of
     * a literal, a call's arguments, the value a field, element, slice or `@` reads from, the branches of `if`/`??`.
     * Each token says so itself ([ITok.pointerFlow]), so that a new kind of expression can't be left out by mistake.
     */
    private fun flows(t: ITok): List<ITok> = when (val flow = t.pointerFlow()) {
        is PointerFlow.From -> flow.values
        PointerFlow.Nothing, PointerFlow.NotAValue -> listOf()
    }

    /** [analyze] of the stage of `value |> stage`, with `it` standing for the value. */
    private fun <R> piped(cd: CompileData, t: TPipe, analyze: (ITok) -> R): R {
        cd.pipedTokens += t.value
        try { return analyze(t.stage) } finally { cd.pipedTokens.removeAt(cd.pipedTokens.size - 1) }
    }

    /** [analyze] of what `it` stands for (in the scope of pipes around that value), or null outside a pipe. */
    private fun <R> unpiped(cd: CompileData, analyze: (ITok) -> R): R? {
        val value = cd.pipedTokens.removeLastOrNull() ?: return null
        try { return analyze(value) } finally { cd.pipedTokens += value }
    }

    /** The memory of named pools (not `pool do` blocks or the current pool) the value of [tok] may be in. */
    fun allocations(cd: CompileData, tok: ITok): Set<Alloc> = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.allocationsOf(Util.testIdentifier(t.location, t.name))
        is TAddressOf -> placeAllocations(cd, t.expr)
        is TInterpolated -> setOfNotNull(allocIn(cd, null))
        is TReadPointer -> allocations(cd, t.expr) + pointedAt(cd, t.expr)
        is TAccessor -> allocations(cd, t.expr) + pointedAt(cd, t.expr)
        is TAccessorNamed -> allocations(cd, t.expr) + if (t.throughPointer) pointedAt(cd, t.expr) else setOf()
        is TNew -> setOfNotNull(allocIn(cd, t.pool)) + flows(t).flatMap { allocations(cd, it) }
        is TExprCall -> t.pools.mapNotNull { allocIn(cd, it) }.toSet() + t.aliased.flatMapIndexed { i, arg ->
            allocations(cd, arg).map { a -> a.copy(viaCall = true, via = viaCall(t, i, a)) }
        }
        is TCallValue -> flows(t).flatMap { allocations(cd, it) }.map { it.copy(viaCall = true) }.toSet()
        is TPipe -> piped(cd, t) { allocations(cd, it) }
        is TIt -> unpiped(cd) { allocations(cd, it) } ?: setOf()
        is TLambda -> (if (t.allocates) setOfNotNull(allocIn(cd, null)) else setOf()) + flows(t).flatMap { allocations(cd, it) }
        else -> flows(t).flatMap { allocations(cd, it) }.toSet()
    }

    /**
     * The calls the memory [a] reaches the result of [call] through, as its argument [i] (see [Alloc.via]), or null when one of
     * them isn't an eclang function's, whose result [Escapes] can't know.
     */
    private fun viaCall(call: TExprCall, i: Int, a: Alloc): Via? {
        val key = call.calleeKey ?: return null
        val param = call.aliasedParams.getOrNull(i) ?: return null
        if (a.viaCall && a.via == null) return null
        return Via(key, call.name, param, a.via)
    }

    /** The named-pool memory the place [tok] (what `addrof` points at) is in. */
    private fun placeAllocations(cd: CompileData, tok: ITok): Set<Alloc> = when (val t = tok.unnested()) {
        is TCastNotNull -> placeAllocations(cd, t.expr)
        is TAccessorNamed -> if (t.throughPointer) allocations(cd, t.expr) else placeAllocations(cd, t.expr)
        is TAccessor, is TReadPointer -> flows(t).flatMap { allocations(cd, it) }.toSet()
        is TNamed -> setOf()
        else -> allocations(cd, t)
    }

    /** What the variables [tok] may point at hold: what reading through [tok] may give. */
    private fun pointedAt(cd: CompileData, tok: ITok): Set<Alloc> = pointees(cd, tok).flatMap { cd.ctx().stack.allocationsOf(it) }.toSet()

    /** The local variables the value of [tok] may point at, so that a write through it changes them. */
    private fun pointees(cd: CompileData, tok: ITok): Set<String> = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.pointeesOf(Util.testIdentifier(t.location, t.name))
        is TAddressOf -> localRoot(cd, t.expr)?.let { setOf(it) } ?: flows(t.expr.unnested()).flatMap { pointees(cd, it) }.toSet()
        is TPipe -> piped(cd, t) { pointees(cd, it) }
        is TIt -> unpiped(cd) { pointees(cd, it) } ?: setOf()
        else -> flows(t).flatMap { pointees(cd, it) }.toSet()
    }

    /** Memory allocated now in the pool [tok] (null: the current pool), or null when no code here can free that pool. */
    private fun allocIn(cd: CompileData, tok: ITok?): Alloc? {
        val pool = (if (tok == null) currentPool(cd) else poolId(cd, tok)) ?: return null
        if (pool == CALLER_POOL) return null
        return cd.ctx().stack.alloc(pool, tok?.let { shown(it) } ?: "pool")
    }

    /** The current pool, as a pool of [ContextStack.poolOf]. */
    private fun currentPool(cd: CompileData): String = cd.poolOrigin()?.pool ?: CALLER_POOL

    /** Whether [pool] is one that something else frees (see [poolId]): the caller's, or that of a `pool do` block. */
    private fun managed(cd: CompileData, pool: String) = pool == CALLER_POOL || pool in cd.ctx().stack.blockPools

    /** The variable or field [tok] names as `x.a.b` (see [ContextStack.poolOf]), or null when it isn't a place the compiler can name. */
    private fun path(cd: CompileData, tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> Util.testIdentifier(t.location, t.name).takeIf { cd.findVar(it) != null }
        is TAccessorNamed -> path(cd, t.expr)?.let { "${pointedVariable(cd, it)}.${t.fixedName}" }
        is TReadPointer -> path(cd, t.expr)?.let { pointedVariable(cd, it) }
        is TCastNotNull -> path(cd, t.expr)
        else -> null
    }

    /** The variable that the pointer [name] always points at, or [name] itself: `hp.p` is `h.p` after `val hp = addrof h`. */
    private fun pointedVariable(cd: CompileData, name: String): String = cd.ctx().stack.pointeesOf(name).singleOrNull() ?: name

    /**
     * The pool the Pool value [tok] is, or null for a new or unknown one. A value that is one of several pools (`if c do pool
     * else p`) counts as the one something else frees, if any: it must not be freed here.
     */
    fun poolId(cd: CompileData, tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) currentPool(cd) else path(cd, t)?.let { cd.ctx().stack.poolOf(it) }
        is TAccessorNamed, is TReadPointer -> path(cd, t)?.let { cd.ctx().stack.poolOf(it) }
        is TCastNotNull -> poolId(cd, t.expr)
        else -> flows(t).mapNotNull { poolId(cd, it) }.firstOrNull { managed(cd, it) }
    }

    /**
     * `const assert a outlives b`: why the memory [a] points into can't be shown to live as long as what [b] points into, or null
     * when it does, as far as the compiler follows memory. Local memory is freed when its scope ends, so a's must be in the same
     * scope as b's or outside it; memory of a pool that code here frees (`new Pool`) must be in a pool b is in too.
     */
    fun outlivesProblem(cd: CompileData, a: ITok, b: ITok, shownA: String, shownB: String): String? {
        val la = value(cd, a)
        val lb = value(cd, b)
        if (la != null && (lb == null || la.depth > lb.depth))
            return "'$shownA' points to ${la.memory}, which is freed ${la.ends}, " + (if (lb == null) "but '$shownB' may live after this function returns." else "before ${lb.memory}, which '$shownB' points to.")
        val poolsB = allocations(cd, b).map { it.pool }.toSet()
        allocations(cd, a).firstOrNull { it.pool !in poolsB }?.let {
            return "'$shownA' may hold memory of the pool ${it.poolName}, which this function can free (`clearPool`, `destroy`) while '$shownB' still holds what it had."
        }
        return null
    }

    /**
     * `const assert x in p`: why the memory [x] points into can't be shown to be in the pool [p] (or `pool`, the current pool),
     * or null when it is. x must point into something, and into nothing else: a local, a parameter's memory or a global isn't in p.
     */
    fun inPoolProblem(cd: CompileData, x: ITok, p: ITok, shownX: String): String? {
        val shownP = shown(p)
        val pool = poolId(cd, p) ?: return "the compiler can't tell which pool '$shownP' is. Name a pool variable, a field holding one, or `pool`."
        val allocs = allocations(cd, x)
        val local = value(cd, x)
        val sources = sources(cd, x)
        if (pool == CALLER_POOL) {
            if (local != null) return "'$shownX' points to ${local.memory}, not to the caller's pool."
            allocs.firstOrNull()?.let { return "'$shownX' may hold memory of the pool ${it.poolName}." }
            if (POOL_SOURCE !in sources) return "nothing shows that '$shownX' was allocated in the current pool (by `new`, `\${...}` or a call)."
            (sources - POOL_SOURCE).firstOrNull()?.let { return "'$shownX' may also point to what ${if (it == GLOBAL_SOURCE) "a global" else "'${Escapes.base(it)}'"} points to." }
            return null
        }
        if (pool in cd.ctx().stack.blockPools) {
            if (local?.pool != pool) return if (local == null) "nothing shows that '$shownX' points into the pool of that `pool` block." else "'$shownX' points to ${local.memory}."
            allocs.firstOrNull { it.pool != pool }?.let { return "'$shownX' may also hold memory of the pool ${it.poolName}." }
            return null
        }
        if (local != null) return "'$shownX' points to ${local.memory}, not to the pool '$shownP'."
        if (allocs.isEmpty()) return "nothing shows that '$shownX' was allocated in '$shownP'."
        allocs.firstOrNull { it.pool != pool }?.let { return "'$shownX' may hold memory of the pool ${it.poolName} instead." }
        return null
    }

    /** How the pool [tok] is written, for errors. */
    fun shown(tok: ITok): String = when (val t = tok.unnested()) {
        is TNamed -> t.name
        is TAccessorNamed -> "${shown(t.expr)}.${t.name}"
        is TReadPointer -> if (t.mutParameter) shown(t.expr) else "@${shown(t.expr)}"
        is TCastNotNull -> shown(t.expr)
        else -> "pool"
    }

    /**
     * After `key = value`, with [key] a variable or field of [type]: a pool variable holds that pool, and the Pool fields
     * of a record hold those of [value]. A pool made by a call is freed with the pools it is given (the current one by default).
     */
    private fun trackPools(cd: CompileData, key: String, type: Type, value: ITok?) {
        val stack = cd.ctx().stack
        if (cd.types.nonNull(type) === cd.types.pool) {
            val pool = value?.let { poolId(cd, it) }
            stack.setPool(key, pool)
            if (pool != null) return
            val made = value?.let { madePools(it) }
            if (made != null) {
                // `new Pool` (or null): this function made it, so it may free it, and freeing its parent frees it too.
                stack.ownedPools += stack.poolOf(key)
                stack.setPoolParents(stack.poolOf(key), made.mapNotNull { if (it.pool == null) currentPool(cd) else poolId(cd, it.pool) }.toSet())
            }
            val call = value?.unnested() as? TExprCall
            if (call != null) stack.setPoolParents(stack.poolOf(key), call.pools.mapNotNull { if (it == null) currentPool(cd) else poolId(cd, it) }.toSet())
        } else if (type is Record && !type.isUnion) {
            type.fields.forEach { (name, field) -> trackPools(cd, "$key.$name", field, value?.let { fieldOf(it, name) }) }
        }
    }

    /**
     * The `new Pool`s the value [tok] is one of, when it is always a pool made there or null (`if c do new Pool else null`):
     * a pool this function made. Null otherwise.
     */
    private fun madePools(tok: ITok): List<TNew>? = when (val t = tok.unnested()) {
        is TNew -> if (t.makesPool) listOf(t) else null
        is TNull -> listOf()
        is TInlineIf -> madePools(t.body)?.let { a -> madePools(t.elseBody)?.let { b -> a + b } }
        else -> null
    }

    /** The value of field [name] of the record [tok]. */
    private fun fieldOf(tok: ITok, name: String): ITok? = when (val t = tok.unnested()) {
        is TCreateRecord -> t.defaults.given[name]
        is TCreateRecordTyped -> t.defaults.given[name]
        is Tok -> TAccessorNamed(t.location, t, name)
        else -> null
    }

    /**
     * Whether this function may clear or destroy the pool [tok] without `unsafe`: it made it with `new Pool` (also under
     * another name, or in a field of a local), or, for [clear], it is the pool of a `pool do` block running here. Any other
     * pool (a parameter, a global, a field of what a pointer points at, a call's result) may hold memory that code elsewhere
     * still uses.
     */
    fun owns(cd: CompileData, tok: ITok, clear: Boolean): Boolean {
        val pool = poolId(cd, tok) ?: return false
        val stack = cd.ctx().stack
        return pool in stack.ownedPools || (clear && pool in stack.blockPools)
    }

    /** Why [tok] isn't a pool this function made, for errors: "'p' is a parameter, which the caller may still use". */
    fun notOwned(cd: CompileData, tok: ITok): String {
        val shown = shown(tok)
        val t = tok.unnested()
        val root = generateSequence(t) { (it as? TAccessorNamed)?.expr?.unnested() ?: (it as? TReadPointer)?.expr?.unnested() ?: (it as? TCastNotNull)?.expr?.unnested() }.last()
        val v = (root as? TNamed)?.let { cd.findVar(Util.testIdentifier(it.location, it.name)) }
        val direct = root === t || t is TCastNotNull
        val name = (root as? TNamed)?.name
        fun kind(what: String) = if (direct) "'$name' is a $what" else "'$shown' is reached through the $what '$name'"
        return when {
            root !is TNamed || v == null -> "it is the result of an expression"
            cd.ctx().stack.getIndexRaw(v.getName()) == null -> "${kind("global")}, which other functions may use too"
            cd.function?.params?.any { it.name == root.name } == true -> "${kind("parameter")}, which the caller may still use"
            else -> "'$shown' holds a pool from elsewhere"
        }
    }

    /**
     * `clearPool(tok)` or, with [free], `destroy tok` at [location]: what was allocated in that pool so far is freed,
     * and with [free] the pool can't be used any more. A global, or what a pointer parameter points at, can't be left pointing
     * there: no code here can tell it did. [pool] is the pool [tok] holds (asked before it was given a new value).
     */
    fun kill(cd: CompileData, location: Loc, tok: ITok, free: Boolean, pool: String? = poolId(cd, tok), how: String? = null) {
        val stack = cd.ctx().stack
        if (pool == null) return
        val verb = if (free) "destroy ${shown(tok)}" else "clearPool(${shown(tok)})"
        val canHere = if (how != null) "Can't give the pool '${shown(tok)}' away here" else "Can't `$verb` here"
        val freed = stack.freedWith(pool)
        stack.scopeStack.last().let { top ->
            top.allocations.forEach { (holder, allocs) ->
                val alloc = allocs.firstOrNull { it.pool in freed } ?: return@forEach
                if (holder.startsWith(BEHIND_PARAMETER)) {
                    val place = holder.removePrefix(BEHIND_PARAMETER)
                    throw EcMemoryError(location, "$canHere: `$place` is the caller's memory and still points to memory of pool '${alloc.poolName}', " +
                        "so the caller would be left pointing to freed memory. Give it another value first, or store a copy in a pool that lives on: `$place = clone(..., other)`.")
                }
                if (top.vars[holder] == null) throw EcMemoryError(location, "$canHere: the global '$holder' still points to memory of pool '${alloc.poolName}', " +
                    "so it would be left pointing to freed memory. Give it another value first, or store a copy in a pool that lives on: `$holder = clone(..., other)`.")
            }
            // The pool itself, kept where this function can't see what happens to it next: it would be destroyed again, or used.
            if (free) top.pools.forEach { (holder, held) ->
                if (held !in freed || holder == path(cd, tok)) return@forEach
                val root = holder.substringBefore('.')
                val global = top.vars[root] == null && cd.findVar(root) != null
                val behindPointer = holder != root && cd.function?.params?.any { it.name == root && cd.types.nonNull(it.type) is Pointer } == true
                if (!global && !behindPointer) return@forEach
                val whose = if (global) "the global '$holder'" else "`$holder`, the caller's memory,"
                throw EcMemoryError(location, "$canHere: $whose still holds this pool, and would be left with a pool that was freed. " +
                    "Give it another value first (`$holder = null`, which needs a `Pool?`), or leave the pool to whoever uses it there.")
            }
        }
        stack.kill(pool, how ?: "`$verb` at line ${location.getLine()}", free)
    }

    /** Checks that the variable [name] (read or changed at [location]) doesn't point to pool memory that was freed. */
    fun checkAlive(cd: CompileData, location: Loc, name: String, shown: String) {
        val stack = cd.ctx().stack
        if (cd.findVar(name)?.getType() === cd.types.pool) checkPool(cd, location, name, shown)
        for (alloc in stack.allocationsOf(name)) {
            alloc.carried?.let { round ->
                if (round.open) round.reads += CarriedRead(location, shown, alloc.poolName, stack.killedSince(round))
            }
            val how = stack.killedBy(alloc) ?: continue
            throw EcMemoryError(location, "Can't use '$shown' here: it points to memory of pool '${alloc.poolName}' that $how freed${throughCalls(alloc)}. " +
                "Use it before the pool is cleared or freed, or keep a copy in a pool that lives on: `clone($shown, other)`.")
        }
    }

    /** How memory [a] got where it is, when through calls: both ends of the chain, as errors about lifetimes should name them. */
    fun throughCalls(a: Alloc): String {
        if (!a.viaCall) return ""
        val via = a.via ?: return " (it came out of a call given that memory: the result of a function value or a C function may point into any of its arguments)"
        val inner = generateSequence(via) { it.next }.last()
        return " (it came out of ${via.shown()}, and the result of `${inner.callee}` may point into what its parameter '${inner.param}' was given, which was that memory)"
    }

    /** Checks that the pool [name] (see [ContextStack.poolOf]) used at [location], written [shown], wasn't freed. */
    private fun checkPool(cd: CompileData, location: Loc, name: String, shown: String) {
        val stack = cd.ctx().stack
        val pool = stack.poolOf(name)
        stack.freedBy(pool)?.let { how ->
            throw EcMemoryError(location, "Can't use the pool '$shown' here: $how freed it. Use it before freeing it, or make a new one first (`$shown = new Pool`, which needs a `var`).")
        }
        stack.rounds.forEach { if (it.open) it.poolReads += PoolRead(location, shown, name, pool) }
    }

    /** `x.field` of type Pool, compiled at [location], must not be a pool that was freed. */
    fun checkPoolField(cd: CompileData, location: Loc, tok: ITok) = path(cd, tok)?.let { checkPool(cd, location, it, shown(tok)) }

    /** `@p` or `p.field` at [location] reads through the pointer [tok]: what it points at must not point to freed memory either. */
    fun checkThrough(cd: CompileData, location: Loc, tok: ITok) = pointees(cd, tok).forEach { checkAlive(cd, location, it, it) }

    /** A loop's first round starts here (see [LoopRound]). */
    fun loopStarts(cd: CompileData) = cd.ctx().stack.openRound { name -> cd.findVar(name)?.getType()?.let { holdsPointer(it) } == true }

    /** A round of the loop [out] loops out from the innermost ends here, at a `continue`. */
    fun roundEnds(cd: CompileData, out: Int) {
        val stack = cd.ctx().stack
        stack.rounds[stack.rounds.size - 1 - out].ends += stack.roundEnd()
    }

    /** A `break` here leaves the loop [out] loops out from the innermost, after running what it defers. */
    fun loopBroken(cd: CompileData, out: Int) {
        val stack = cd.ctx().stack
        stack.rounds[stack.rounds.size - 1 - out].breaks += stack.roundEnd()
    }

    /** The body of the innermost loop has been compiled: checks what a round leaves for the next one. */
    fun loopCompiled(cd: CompileData) {
        val stack = cd.ctx().stack
        val round = stack.rounds.last()
        round.open = false
        stack.mergeBreaks(round.breaks)
        if (cd.functionHasErrors) return
        checkCarried(cd, round, round.ends + stack.roundEnd())
        if (stack.rounds.size == 1 && !cd.functionHasErrors) replay(cd)
    }

    /** The innermost loop is done with, also when compiling it failed. */
    fun loopLeft(cd: CompileData) {
        val stack = cd.ctx().stack
        stack.rounds.removeLastOrNull()
        if (stack.rounds.isEmpty()) stack.replays.clear()
    }

    /** Reports the uses in [round] of what a round ending at one of [ends] leaves in a variable, or of a pool it freed. */
    private fun checkCarried(cd: CompileData, round: LoopRound, ends: List<RoundEnd>) {
        for (read in round.reads) for (end in ends) {
            val alloc = end.allocations[read.variable].orEmpty().firstOrNull { it.carried == null && (it.pool in read.killed || end.killed[it.pool].let { kill -> kill != null && kill.seq > it.seq }) } ?: continue
            val how = (read.killed[alloc.pool] ?: end.killed[alloc.pool])!!.how
            cd.report(EcError(read.location, "Can't use '${read.shown}' here: from the second round of the loop on, it points to memory of pool '${alloc.poolName}' that $how freed. " +
                "Give it a new value before this use, or keep a copy in a pool that lives on: `clone(..., other)`."))
        }
        for (read in round.poolReads) {
            if (cd.findVar(read.variable.substringBefore('.')) == null) continue // defined in the loop, so new in every round
            for (end in ends) {
                val how = end.freed[read.pool]?.takeIf { (end.pools[read.variable] ?: read.pool) == read.pool } ?: continue
                cd.report(EcError(read.location, "Can't use the pool '${read.shown}' here: from the second round of the loop on, $how has freed it. Make a new one in the loop, or free it after the loop."))
            }
        }
    }

    /**
     * Makes the step [run] of following local memory, and runs it again in the scopes it was made in when the outermost loop
     * around it has been compiled (see [replay]). [run] may read and write what is followed, and must do nothing else.
     */
    private fun followed(cd: CompileData, run: () -> Unit) {
        // Also as if the loops around gave each round a pool (see LoopPools), when that isn't already the case.
        val loops = if (cd.ctx().stack.imagining) listOf() else cd.loopPools.toList()
        val step = { run(); LoopPools.imagine(cd, loops, run) }
        step()
        val stack = cd.ctx().stack
        if (stack.rounds.isNotEmpty()) stack.replays += Replay(stack.scopeStack.toList(), cd.poolBlocks.toList(), cd.pipedTokens.toList(), step)
    }

    /**
     * The local memory that memory allocated in the current pool now is in: the innermost `pool do` block's, or, while
     * [ContextStack.imagining], that of the round of a loop of `main` (see [LoopPools]); null for the caller's pool.
     */
    private fun currentPoolMemory(cd: CompileData): Local? =
        cd.poolOrigin() ?: if (cd.ctx().stack.imagining && cd.loopPools.isNotEmpty()) LoopPools.memory(cd.loopPools) else null

    /**
     * What a loop's body does to variables reaches the code before it in the next round. So its steps run again, as more
     * rounds would, until what they find settles; a check that fails then is an error of a later round.
     */
    private fun replay(cd: CompileData) {
        val stack = cd.ctx().stack
        val (blocks, pipes) = cd.poolBlocks.toList() to cd.pipedTokens.toList()
        do {
            val before = stack.followed()
            for (step in stack.replays.toList()) {
                cd.poolBlocks.clear(); cd.poolBlocks += step.poolBlocks
                cd.pipedTokens.clear(); cd.pipedTokens += step.pipes
                try {
                    stack.inScopes(step.scopes, step.run)
                } catch (e: EcError) {
                    stack.replays -= step
                    cd.report(EcMemoryError(e.location, "${e.reason} This happens from the second round of the loop on, with the value an earlier round left."))
                } finally {
                    cd.poolBlocks.clear(); cd.poolBlocks += blocks
                    cd.pipedTokens.clear(); cd.pipedTokens += pipes
                }
            }
        } while (stack.followed() != before)
    }

    /**
     * The local memory that the place [tok] (what `addrof` points at) is in, or null when it isn't local. The variable behind a
     * `mut` parameter p is the caller's, but the function may not keep a pointer to it, as the caller passed it for the call only,
     * so `addrof p` counts as local here. Not for [store], the place an assignment writes: that is the caller's, which outlives the call.
     */
    fun place(cd: CompileData, tok: ITok, store: Boolean = false): Local? = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.definedDepth(Util.testIdentifier(t.location, t.name))?.let { depth ->
            val kind = if (cd.ctx().getRaw(Util.testIdentifier(t.location, t.name))?.kind() == "a parameter") "parameter" else "variable"
            Local(depth, "'${t.name}', a local $kind", "when its block ends",
                "Allocate the value in a pool instead: `new ${t.name}` is a copy of it in the current pool (`new ${t.name} in p` in pool p).")
        }
        is TCastNotNull -> place(cd, t.expr, store)
        is TAccessorNamed -> if (t.throughPointer) value(cd, t.expr) else place(cd, t.expr, store)
        is TAccessor -> if (t.inValue) place(cd, t.expr, store) else value(cd, t.expr) // elements are behind a pointer, except those of a `T[N]`
        is TReadPointer -> (if (t.mutParameter && !store) mutParameter(cd, t) else null) ?: value(cd, t.expr)
        // A literal has a place of its own, which C frees when the block ends.
        else -> if (isLiteral(t)) deepest(listOf(temporary(cd), value(cd, t))) else null
    }

    /** The caller's variable behind the `mut` parameter `@p` [t] (see [place]): it can't outlive the call. */
    private fun mutParameter(cd: CompileData, t: TReadPointer): Local? {
        val p = t.expr.unnested() as? TNamed ?: return null
        val depth = cd.ctx().stack.definedDepth(Util.testIdentifier(p.location, p.name)) ?: return null
        return Local(depth, "'${p.name}', the caller's variable passed as `mut ${p.name}`", "when the call returns",
            "A `mut` parameter is the caller's variable for the length of the call only. Keep a copy of its value instead: `new ${p.name}` is one in the current pool.")
    }

    /** Whether [tok] (unnested) makes a value with a place of its own, unlike a variable or a call. */
    fun isLiteral(tok: ITok) = tok is TCreateRecord || tok is TCreateRecordTyped || tok is TCreateTuple || tok is TCreateArray || tok is TCreateCArray

    /** The memory [tok], a pool given to `new` or a call (null: the current pool), is freed with. */
    private fun pool(cd: CompileData, tok: ITok?): Local? = if (tok == null) currentPoolMemory(cd) else value(cd, tok)

    /** The depth is that of the eclang scope, so C must keep a literal for as long: see [CompileData.keepingLiterals]. */
    private fun arrayLiteral(cd: CompileData): Local? = if (!cd.inBody) null else
        Local(cd.ctx().depth(), "the elements of an array literal (on the stack)", "when its block ends",
            "Copy it into a pool with `clone(...)`, e.g. `clone([1, 2, 3])`.", holder = true)

    private fun temporary(cd: CompileData): Local? = if (!cd.inBody) null else
        Local(cd.ctx().depth(), "a temporary value (on the stack)", "when its block ends", "Allocate it in a pool instead: `new` of the value, e.g. `new Point { x = 1 }`.")

    /**
     * The memory freed first (the deepest scope) of [locals]. When that is the storage of an array, what it holds also
     * includes the others, as a value that may be in either may be read from either.
     */
    fun deepest(locals: List<Local?>): Local? {
        val all = locals.filterNotNull()
        val first = all.maxByOrNull { it.depth } ?: return null
        val others = all.filter { it !== first }
        return if (!first.holder || others.isEmpty()) first else first.holding(deepest(others.map { it.stored() } + first.inside))
    }

    /** Whether the value of [tok], already compiled, may use an array literal, which is on the stack of the code it is compiled into. */
    fun usesStack(tok: ITok): Boolean = when (val t = tok.unnested()) {
        is TCreateArray -> !t.isFixed || t.values.any { usesStack(it) }
        is TCreateCArray -> true
        is TCreateRecord -> t.defaults.onStack || flows(t).any { usesStack(it) }
        is TCreateRecordTyped -> t.defaults.onStack || flows(t).any { usesStack(it) }
        else -> flows(t).any { usesStack(it) }
    }

    /** The local memory a `pool do` block at [line] (opened at scope [depth]) frees when it ends. [pool] is its pool. */
    fun poolBlock(depth: Int, line: Int, pool: String) = Local(depth, "memory from the pool of the `pool` block at line $line", "when that block ends",
        "Keep a copy instead: `keep(x)` copies x into the pool the function was called with, which outlives its `pool` blocks.", pool = pool)

    /** How [tok] is written, when it is a name or a field of one (`x`, `o.name`), for the `keep(...)` an error suggests; null otherwise. */
    fun spelled(tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> t.name
        is TAccessorNamed -> spelled(t.expr)?.let { "$it.${t.name}" }
        is TReadPointer -> if (t.mutParameter) spelled(t.expr) else null
        else -> null
    }

    /** `ret value` (also `throw`, and a lambda's result): [value] must not point into memory the function frees. */
    fun checkReturn(cd: CompileData, location: Loc, value: ITok, type: Type, verb: String = "return") {
        if (!holdsPointer(type)) return
        noteResult(cd, value)
        followed(cd) {
            value(cd, value)?.takeIf { !LoopPools.keep(it, null) }?.let { local ->
                throw EcMemoryError(location, "Can't $verb this: it points to ${local.memory}, which is freed ${if (local.pool != null) local.ends else "when the function returns"}. ${local.fixFor(spelled(value))}")
            }
        }
    }

    /**
     * The function being compiled (not a lambda) may give its caller [value], by `ret`, `throw` or `try`: its result may point
     * into what [value] does (see [Escapes.resultRoots]).
     */
    fun noteResult(cd: CompileData, value: ITok) {
        val fn = cd.function ?: return
        if (cd.closure == null) cd.escapes.returns(fn, sources(cd, value))
    }

    /** The named pools the value [tok] (of [type]) that a `ret` or `throw` gives needs, taken before its deferred statements are compiled. */
    class Leaving(val allocations: Set<Alloc>, val pool: String?, val shown: String)

    fun leaving(cd: CompileData, tok: ITok, type: Type) = Leaving(if (holdsPointer(type)) allocations(cd, tok) else setOf(), if (type === cd.types.pool) poolId(cd, tok) else null, shown(tok))

    /**
     * After compiling the deferred statements of a `ret` or `throw` at [location]: what it gives must not be what they free.
     * What only may be there, through a call, doesn't count: a function that works in a scratch pool it frees returns the
     * result of calls given what is in that pool, and a copy of that result is often not possible.
     */
    fun checkLeaving(cd: CompileData, location: Loc, value: Leaving, verb: String = "return") {
        val stack = cd.ctx().stack
        value.pool?.let { pool -> stack.freedBy(pool)?.let { how ->
            throw EcMemoryError(location, "Can't $verb the pool '${value.shown}': $how frees it when the function leaves, so it would be a freed pool.")
        } }
        val copy = "Copy it into a pool that lives on: `clone(x, other)`, or `clone(x)` for the current pool."
        for (alloc in value.allocations) {
            val how = stack.killedBy(alloc) ?: continue
            // Through a call, it is known once every body is compiled whether the result points into what the call was given.
            if (alloc.viaCall) {
                val via = alloc.via ?: continue
                val inner = generateSequence(via) { it.next }.last()
                cd.escapes.leaving(location, "Can't $verb this: it comes from ${via.shown()}, and the result of `${inner.callee}` points into what its parameter '${inner.param}' " +
                    "was given here, memory of pool '${alloc.poolName}', and $how frees that when the function leaves, after the value is read, so it would point to freed memory. $copy", via)
                continue
            }
            throw EcMemoryError(location, "Can't $verb this: it points to memory of pool '${alloc.poolName}', and $how frees that when the function leaves, after the value is read, so it would point to freed memory. $copy")
        }
    }

    /**
     * Storing [value] (of [type]) in a place that lives until the scope at [targetDepth] ends (null: longer than the
     * function, e.g. a global or memory behind a pointer). [target] names the place in errors.
     */
    fun checkStore(cd: CompileData, location: Loc, value: ITok, type: Type, targetDepth: Int?, target: String): Local? {
        if (!holdsPointer(type)) return null
        val local = value(cd, value) ?: return null
        if (targetDepth != null && local.depth <= targetDepth) return local
        if (LoopPools.keep(local, targetDepth)) return local
        val lives = if (targetDepth == null) "but $target outlives this function" else "before $target is"
        throw EcMemoryError(location, "Can't store this in $target: it points to ${local.memory}, which is freed ${local.ends}, $lives. ${local.fixFor(spelled(value))}")
    }

    /**
     * `target = value` (of [type]): checks [value] doesn't outlive the place it's stored in, called [what] in errors.
     * A place inside a local variable (`x`, or a field of a record `x` holds) then remembers what it points to.
     */
    fun assign(cd: CompileData, location: Loc, target: ITok, value: ITok, type: Type, what: String) {
        val stack = cd.ctx().stack
        noteStore(cd, target, value, type)
        path(cd, target)?.let { trackPools(cd, it, type, value) }
        trackAllocations(cd, target, value, type)
        followed(cd) {
            val root = localRoot(cd, target)
            val depth = if (root != null) stack.definedDepth(root) else place(cd, target, store = true)?.depth
            // A store through a pointer changes the variables the pointer may point at.
            val changed = if (root != null) setOf(root) else baseVariable(target)?.let { pointees(cd, it) }.orEmpty()
            checkStore(cd, location, value, type, depth, what)?.let { local -> changed.forEach { stack.setOrigin(it, local) } }
            if (holdsPointer(type)) {
                val pointed = pointees(cd, value)
                (changed + listOfNotNull(baseVariable(target)?.let { Util.testIdentifier(it.location, it.name) })).forEach { stack.addPointees(it, pointed) }
            }
        }
    }

    /**
     * After `target = value`: a plain variable now holds what [value] holds, and a variable whose field or memory
     * is set (`x.f = v`, `x.next = v`, `x[i] = v`) also holds what v holds, as does what x may point at.
     */
    private fun trackAllocations(cd: CompileData, target: ITok, value: ITok, type: Type) {
        val stack = cd.ctx().stack
        val t = target.unnested()
        if (t is TNamed) {
            val name = Util.testIdentifier(t.location, t.name)
            if (holdsPointer(type)) stack.setAllocations(name, allocations(cd, value))
            return
        }
        if (!holdsPointer(type)) return
        val base = baseVariable(target) ?: return
        val held = allocations(cd, value)
        (pointees(cd, base) + Util.testIdentifier(base.location, base.name)).forEach { stack.setAllocations(it, held, add = true) }
        storeBehindParameter(cd, target, base, held)
    }

    /**
     * `target = value`, with the pool memory [held], through the parameter [base]: the caller sees this after the function
     * has returned, so [kill] needs to know. It is kept per place, for a new value to replace it. An element has no place of
     * its own. What only may be in a pool, through a call, doesn't count, as in [checkLeaving].
     */
    private fun storeBehindParameter(cd: CompileData, target: ITok, base: TNamed, held: Set<Alloc>) {
        val name = Util.testIdentifier(base.location, base.name)
        if (localRoot(cd, target) != null || cd.ctx().getRaw(name)?.kind() != "a parameter") return
        val stack = cd.ctx().stack
        val certain = held.filter { !it.viaCall && it.carried == null }.toSet()
        val place = path(cd, target)
        if (place == null) stack.setAllocations("$BEHIND_PARAMETER${shown(base)}[...]", certain, add = true)
        else stack.replaceAllocations("$BEHIND_PARAMETER$place", certain)
    }

    /** The variable a place is in or reached from: x for `x.f`, `x.next.f`, `x[i]`, `@x`. */
    private fun baseVariable(tok: ITok): TNamed? = when (val t = tok.unnested()) {
        is TNamed -> t
        is TAccessorNamed -> baseVariable(t.expr)
        is TAccessor -> baseVariable(t.expr)
        is TReadPointer -> baseVariable(t.expr)
        is TCastNotNull -> baseVariable(t.expr)
        else -> null
    }

    /** The local variable whose own memory [tok] is (`x`, `x.field`, not through a pointer), or null. */
    private fun localRoot(cd: CompileData, tok: ITok): String? = when (val t = tok.unnested()) {
        is TNamed -> Util.testIdentifier(t.location, t.name).takeIf { cd.ctx().stack.definedDepth(it) != null }
        is TAccessorNamed -> if (t.throughPointer) null else localRoot(cd, t.expr)
        is TAccessor -> if (t.inValue) localRoot(cd, t.expr) else null
        else -> null
    }

    /**
     * Remembers what memory the local [name] points into after `val name = value`, or with [element] after it is set
     * to an element of the array [value].
     */
    fun define(cd: CompileData, name: String, value: ITok, type: Type, element: Boolean = false) {
        // [name] is the variable's fixed name (see Util.testIdentifier), as it was defined.
        val stack = cd.ctx().stack
        cd.resourceRecord(type)?.let { if (cd.function != null) cd.unusedResources.putIfAbsent(name, ((value as? Tok)?.location ?: Loc.IDENTITY) to it) }
        trackPools(cd, name, type, value)
        if (!holdsPointer(type)) return
        noteLocal(cd, name, value, element)
        followed(cd) {
            stack.resetOrigin(name, value(cd, value).let { if (element) it?.stored() else it })
            stack.addPointees(name, pointees(cd, value))
        }
        stack.setAllocations(name, allocations(cd, value))
    }

    /** Remembers that [name], a `...rest` parameter, is an array on the stack of the caller. */
    fun defineRest(cd: CompileData, name: String) = cd.ctx().stack.setOrigin(name, Local(cd.ctx().depth(), "'$name', the extra arguments of the call (on the caller's stack)",
        "when the call ends", "Copy it into a pool with `clone($name)`.", holder = true))

    /**
     * The arguments (of [types]) that a store through a call's result (of type [result]) may write into: those that can reach what
     * the result points at. `find(m, k).value = v`, with `find` returning an `@Entry`, may write into m when m's type holds Entries,
     * but `answer(s, req).status = 200` doesn't write into a Server that holds no Response. Nothing for a result that points at nothing.
     */
    fun storedInto(args: List<ITok>, types: List<Type>, result: Type): List<ITok> {
        val targets = writtenThrough(if (result is Result) result.value else result)
        if (targets.isEmpty()) return listOf()
        return args.filterIndexed { i, _ -> targets.any { reaches(types[i], it) } }
    }

    /** What a store through a value of [type] writes: what a pointer points at, an array's elements. */
    private fun writtenThrough(type: Type): List<Type> = when (type) {
        is Pointer -> listOf(type.target)
        is ArrayType -> listOf(type.element)
        is Distinct -> writtenThrough(type.base)
        is Union -> type.members.flatMap { writtenThrough(it) }
        else -> listOf()
    }

    /**
     * Whether memory reached from a value of [from] may hold a [target]: following pointers, arrays, fields and union members.
     * An argument that is itself `@Any` may point at anything; an `@Any` field (a C handle, a user's context) only holds an
     * eclang value through an unchecked cast, so it doesn't count.
     */
    private fun reaches(from: Type, target: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (from === target || target === Types.Any) return true
        if (from === Types.Any) return seen.isEmpty() || seen.all { it is Pointer || it is Union }
        if (from is TypeParam || from is Interface || from is Applied) return true
        if (!seen.add(from)) return false
        return when (from) {
            is Pointer -> reaches(from.target, target, seen)
            is ArrayType -> reaches(from.element, target, seen)
            is FixedArray -> reaches(from.element, target, seen)
            is Distinct -> reaches(from.base, target, seen)
            is Union -> from.members.any { reaches(it, target, seen) }
            is Tuple -> from.elements.any { reaches(it, target, seen) }
            is Record -> generateSequence(from) { it.parent }.any { r -> r.fields.values.any { reaches(it, target, seen) } } ||
                (target is Record && generateSequence(target) { it.parent }.any { it === from })
            is Result -> reaches(from.value, target, seen) || reaches(from.error, target, seen)
            else -> false
        }
    }

    /** Whether a value of [type] can point to memory (a pointer, String, array, or something holding one). */
    fun holdsPointer(type: Type, seen: MutableSet<Type> = HashSet()): Boolean {
        if (!seen.add(type)) return false
        return when (type) {
            is Pointer, is ArrayType -> true
            Types.String, Types.CString -> true
            is Distinct -> holdsPointer(type.base, seen)
            is Union -> type.members.any { holdsPointer(it, seen) }
            is Tuple -> type.elements.any { holdsPointer(it, seen) }
            is FixedArray -> holdsPointer(type.element, seen)
            is Record -> type.fields.values.any { holdsPointer(it, seen) }
            is Result -> holdsPointer(type.value, seen) || holdsPointer(type.error, seen)
            // A closure points to what its lambda captured (a C function pointer points to code only).
            is FunctionType -> !type.isC
            else -> false
        }
    }
}
