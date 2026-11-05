package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
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
import github.mrh0.eclang.ast.token.function.call.TExprCall
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
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.util.Util
import github.mrh0.ect2.*

/**
 * Memory that is freed before the function returns: a local variable, the elements of an array literal (C stack
 * memory), or the pool of a `pool do` block. It is freed when the scope at [depth] ends (see `ContextStack.depth`),
 * so a pointer into it may only be kept in variables defined at that depth or deeper.
 * [memory] names it in errors ("'x', a local variable"), [ends] says when it is freed, [fix] how to keep the value.
 * [holder]: it is the storage of an array, so an element read from it doesn't point into it; [inside] is the memory the
 * elements may point into. [pool]: the pool of a `pool do` block (see [ContextStack.poolOf]).
 */
class Local(val depth: Int, val memory: String, val ends: String, val fix: String, val holder: Boolean = false, val inside: Local? = null, val pool: String? = null) {
    /** The memory a value read out of this memory may point into. */
    fun stored(): Local? = if (holder) inside else this

    fun holding(inside: Local?) = Local(depth, memory, ends, fix, holder, inside, pool)
}

/**
 * Catches dangling pointers at compile time: a pointer to memory that is freed when a block or the function ends
 * must not be returned, thrown, or stored where it outlives that memory. Values are followed through variables
 * (`val p = addrof x; ret p`), record and array literals, `new`, pipes, calls (a call's result may point into its
 * arguments, or into the pools it is given), and what is read out of memory: `p.next`, `a[i]`, `a[x..<y]` and `@p`
 * may point where p and a do, since what is stored there must live at least as long as p and a's memory.
 *
 * Memory of a pool that isn't tied to a block (`newPool()`) is followed the same way by [allocations]: after
 * `clearPool(p)` or `freePool(p)`, a variable holding something allocated in p before can't be used. Pools have an
 * identity of their own ([poolId]), so that `val q = p` and a field `h.p` are the same pool, and freeing a pool frees its children.
 *
 * Code is compiled once, in source order, but a loop's body runs again with what the previous round left: see [followed]
 * for local memory, and [LoopRound] for pools.
 */
object Lifetimes {
    /** The current pool outside `pool do` blocks: it belongs to the caller, so it holds what the caller holds. */
    const val CALLER_POOL = "#caller"

    /** The local memory the value of [tok] points into, or null when it lives at least as long as the function call. */
    fun value(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TAddressOf -> place(cd, t.expr)
        is TNamed -> if (TNamed.isCurrentPool(cd, t.name)) cd.poolOrigin() else cd.ctx().stack.originOf(Util.testIdentifier(t.location, t.name))
        is TCreateArray -> deepest(listOf(if (t.isFixed) null else arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TCreateCArray -> deepest(listOf(arrayLiteral(cd)) + flows(t).map { value(cd, it) })
        is TAccessorNamed -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null) + flows(t).map { value(cd, it) })
        // An element is what is stored in the array, unlike a slice, which still points into it.
        is TAccessor -> deepest(listOf(if (t.viewsValue) place(cd, t.expr) else null, value(cd, t.expr).let { if (t.index.unnested() is TRange) it else it?.stored() }))
        is TNew -> deepest(listOf(pool(cd, t.pool)) + flows(t).map { value(cd, it) })
        is TExprCall -> deepest(t.pools.map { pool(cd, it) } + flows(t).map { value(cd, it) })
        is TInterpolated -> cd.poolOrigin()
        is TCreateRecord -> record(cd, t, t.defaults)
        is TCreateRecordTyped -> record(cd, t, t.defaults)
        is TPipe -> piped(cd, t) { value(cd, it) }
        is TIt -> unpiped(cd) { value(cd, it) }
        else -> deepest(flows(t).map { value(cd, it) })
    }

    /** The value of the record literal [t]: its fields, and what the defaults it used ([defaults]) point into. */
    private fun record(cd: CompileData, t: ITok, defaults: RecordDefaults): Local? =
        deepest(listOf(if (defaults.allocates) cd.poolOrigin() else null, if (defaults.onStack) arrayLiteral(cd) else null) + flows(t).map { value(cd, it) })

    /**
     * The values whose memory the value of [t] may point into, besides memory [t] allocates or is itself: the parts of
     * a literal, a call's arguments, the value a field, element, slice or `@` reads from, the branches of `if`/`??`.
     */
    private fun flows(t: ITok): List<ITok> = when (t) {
        is TCreateArray -> t.values
        is TCreateCArray -> t.values
        is TCreateTuple -> t.values
        is TCreateRecord -> t.values
        is TCreateRecordTyped -> t.values
        is TNew -> if (t.copies) listOf(t.value) else listOf()
        is TExprCall -> t.aliased
        is TCallValue -> t.args
        is TInlineIf -> listOf(t.body, t.elseBody)
        is TNullishCoalescing -> listOf(t.left, t.right)
        is TNullishJump -> listOf(t.left)
        is TWhenExpr -> t.flows
        is TSafeCall -> listOfNotNull(t.receiver, t.call)
        is TUpdateRecord -> t.values + t.base
        is TCast -> listOf(t.expr)
        is TCastUnsafe -> listOf(t.expr)
        is TCastNotNull -> listOf(t.expr)
        is TTry -> listOf(t.expr)
        is TAccessor -> listOf(t.expr)
        is TAccessorNamed -> listOf(t.expr)
        is TReadPointer -> listOf(t.expr)
        else -> listOf()
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
        is TExprCall -> t.pools.mapNotNull { allocIn(cd, it) }.toSet() + flows(t).flatMap { allocations(cd, it) }.map { it.copy(viaCall = true) }
        is TCallValue -> flows(t).flatMap { allocations(cd, it) }.map { it.copy(viaCall = true) }.toSet()
        is TPipe -> piped(cd, t) { allocations(cd, it) }
        is TIt -> unpiped(cd) { allocations(cd, it) } ?: setOf()
        else -> flows(t).flatMap { allocations(cd, it) }.toSet()
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

    /** How the pool [tok] is written, for errors. */
    fun shown(tok: ITok): String = when (val t = tok.unnested()) {
        is TNamed -> t.name
        is TAccessorNamed -> "${shown(t.expr)}.${t.name}"
        is TReadPointer -> "@${shown(t.expr)}"
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
            val call = value?.unnested() as? TExprCall
            if (pool == null && call != null) stack.setPoolParents(stack.poolOf(key), call.pools.mapNotNull { if (it == null) currentPool(cd) else poolId(cd, it) }.toSet())
        } else if (type is Record && !type.isUnion) {
            type.fields.forEach { (name, field) -> trackPools(cd, "$key.$name", field, value?.let { fieldOf(it, name) }) }
        }
    }

    /** The value of field [name] of the record [tok]. */
    private fun fieldOf(tok: ITok, name: String): ITok? = when (val t = tok.unnested()) {
        is TCreateRecord -> t.defaults.given[name]
        is TCreateRecordTyped -> t.defaults.given[name]
        is Tok -> TAccessorNamed(t.location, t, name)
        else -> null
    }

    /**
     * `clearPool(tok)` or, with [free], `freePool(tok)` at [location]: what was allocated in that pool so far is freed,
     * and with [free] the pool can't be used any more. A global can't be left pointing there: no code here can tell it did.
     */
    fun kill(cd: CompileData, location: Loc, tok: ITok, free: Boolean) {
        val stack = cd.ctx().stack
        val pool = poolId(cd, tok) ?: return
        val verb = if (free) "freePool" else "clearPool"
        val freed = stack.freedWith(pool)
        stack.scopeStack.last().let { top ->
            top.allocations.forEach { (holder, allocs) ->
                val alloc = allocs.firstOrNull { it.pool in freed } ?: return@forEach
                if (top.vars[holder] == null) throw EcError(location, "Can't call `$verb(${shown(tok)})` here: the global '$holder' still points to memory of pool '${alloc.poolName}', " +
                    "so it would be left pointing to freed memory. Give it another value first, or store a copy in a pool that lives on: `$holder = clone(..., other)`.")
            }
        }
        stack.kill(pool, "`$verb(${shown(tok)})` at line ${location.getLine()}", free)
    }

    /** A loop that assigns, clears or frees the pool variable [name] (in [assigned]) frees its memory for the next round. */
    fun loopKills(cd: CompileData, location: Loc, assigned: Set<String>) = assigned.forEach { raw ->
        val name = Util.testIdentifier(location, raw)
        if (cd.ctx().getRaw(name)?.getType() !== cd.types.pool) return@forEach
        val pool = cd.ctx().stack.poolOf(name)
        cd.ctx().stack.kill(pool, "an earlier round of the loop at line ${location.getLine()} (the loop clears, frees or replaces '$raw')", free = false)
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
            throw EcError(location, "Can't use '$shown' here: it points to memory of pool '${alloc.poolName}' that $how freed. " +
                "Use it before the pool is cleared or freed, or keep a copy in a pool that lives on: `clone($shown, other)`.")
        }
    }

    /** Checks that the pool [name] (see [ContextStack.poolOf]) used at [location], written [shown], wasn't freed. */
    private fun checkPool(cd: CompileData, location: Loc, name: String, shown: String) {
        val stack = cd.ctx().stack
        val pool = stack.poolOf(name)
        stack.freedBy(pool)?.let { how ->
            throw EcError(location, "Can't use the pool '$shown' here: $how freed it. Use it before freeing it, or make a new one first (`$shown = newPool()`, which needs a `var`).")
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

    /** The body of the innermost loop has been compiled: checks what a round leaves for the next one. */
    fun loopCompiled(cd: CompileData) {
        val stack = cd.ctx().stack
        val round = stack.rounds.last()
        round.open = false
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
        run()
        val stack = cd.ctx().stack
        if (stack.rounds.isNotEmpty()) stack.replays += Replay(stack.scopeStack.toList(), cd.poolBlocks.toList(), cd.pipedTokens.toList(), run)
    }

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
                    cd.report(EcError(e.location, "${e.reason} This happens from the second round of the loop on, with the value an earlier round left."))
                } finally {
                    cd.poolBlocks.clear(); cd.poolBlocks += blocks
                    cd.pipedTokens.clear(); cd.pipedTokens += pipes
                }
            }
        } while (stack.followed() != before)
    }

    /** The local memory that the place [tok] (what `addrof` points at) is in, or null when it isn't local. */
    fun place(cd: CompileData, tok: ITok): Local? = when (val t = tok.unnested()) {
        is TNamed -> cd.ctx().stack.definedDepth(Util.testIdentifier(t.location, t.name))?.let { depth ->
            val kind = if (cd.ctx().getRaw(Util.testIdentifier(t.location, t.name))?.kind() == "a parameter") "parameter" else "variable"
            Local(depth, "'${t.name}', a local $kind", "when its block ends",
                "Allocate the value in a pool instead: `new ${t.name}` is a copy of it in the current pool (`new ${t.name} in p` in pool p).")
        }
        is TCastNotNull -> place(cd, t.expr)
        is TAccessorNamed -> if (t.throughPointer) value(cd, t.expr) else place(cd, t.expr)
        is TAccessor -> if (t.inValue) place(cd, t.expr) else value(cd, t.expr) // elements are behind a pointer, except those of a `T[N]`
        is TReadPointer -> value(cd, t.expr)
        // A literal has a place of its own, which C frees when the block ends.
        else -> if (isLiteral(t)) deepest(listOf(temporary(cd), value(cd, t))) else null
    }

    /** Whether [tok] (unnested) makes a value with a place of its own, unlike a variable or a call. */
    fun isLiteral(tok: ITok) = tok is TCreateRecord || tok is TCreateRecordTyped || tok is TCreateTuple || tok is TCreateArray || tok is TCreateCArray

    /** The memory [tok], a pool given to `new` or a call (null: the current pool), is freed with. */
    private fun pool(cd: CompileData, tok: ITok?): Local? = if (tok == null) cd.poolOrigin() else value(cd, tok)

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
        "Code in a `pool` block allocates in its pool, and so do the functions it calls. To keep a value, copy it into a pool that outlives the block: write `val outer = pool` before the block, then `clone(x, outer)` (or `new ... in outer`).", pool = pool)

    /** `ret value` (also `throw`, and a lambda's result): [value] must not point into memory the function frees. */
    fun checkReturn(cd: CompileData, location: Loc, value: ITok, type: Type, verb: String = "return") {
        if (!holdsPointer(type)) return
        followed(cd) {
            value(cd, value)?.let { local ->
                throw EcError(location, "Can't $verb this: it points to ${local.memory}, which is freed when the function returns, so it would point to freed memory. ${local.fix}")
            }
        }
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
            throw EcError(location, "Can't $verb the pool '${value.shown}': $how frees it when the function leaves, so it would be a freed pool.")
        } }
        for (alloc in value.allocations) {
            val how = stack.killedBy(alloc)?.takeIf { !alloc.viaCall } ?: continue
            throw EcError(location, "Can't $verb this: it points to memory of pool '${alloc.poolName}', and $how frees that when the function leaves, after the value is read, so it would point to freed memory. " +
                "Copy it into a pool that lives on: `clone(x, other)`, or `clone(x)` for the current pool.")
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
        val lives = if (targetDepth == null) "$target outlives this function" else "$target lives longer"
        throw EcError(location, "Can't store this in $target: it points to ${local.memory}, which is freed ${local.ends}, but $lives, so it would be left pointing to freed memory. ${local.fix}")
    }

    /**
     * `target = value` (of [type]): checks [value] doesn't outlive the place it's stored in, called [what] in errors.
     * A place inside a local variable (`x`, or a field of a record `x` holds) then remembers what it points to.
     */
    fun assign(cd: CompileData, location: Loc, target: ITok, value: ITok, type: Type, what: String) {
        val stack = cd.ctx().stack
        path(cd, target)?.let { trackPools(cd, it, type, value) }
        trackAllocations(cd, target, value, type)
        followed(cd) {
            val root = localRoot(cd, target)
            val depth = if (root != null) stack.definedDepth(root) else place(cd, target)?.depth
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
        trackPools(cd, name, type, value)
        if (!holdsPointer(type)) return
        followed(cd) {
            stack.resetOrigin(name, value(cd, value).let { if (element) it?.stored() else it })
            stack.addPointees(name, pointees(cd, value))
        }
        stack.setAllocations(name, allocations(cd, value))
    }

    /** Remembers that [name], a `...rest` parameter, is an array on the stack of the caller. */
    fun defineRest(cd: CompileData, name: String) = cd.ctx().stack.setOrigin(name, Local(cd.ctx().depth(), "'$name', the extra arguments of the call (on the caller's stack)",
        "when the call ends", "Copy it into a pool with `clone($name)`.", holder = true))

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
            else -> false
        }
    }
}
