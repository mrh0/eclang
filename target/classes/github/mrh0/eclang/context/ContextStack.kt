package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.ect2.Type

/**
 * Memory allocated in [pool] ([poolName] in code) at point [seq] of the function, which `clearPool`/`destroy` of it later frees.
 * With [carried] it stands for "what the variable [poolName] held when the previous round of that loop ended" instead: see [LoopRound].
 * [viaCall]: a value only may be in it, because it came from a call given something that is. [via] then says which calls, when
 * each is of an eclang function ([Via]): once every body is compiled, [Escapes] knows whether the value really can be in it.
 */
data class Alloc(val seq: Int, val pool: String, val poolName: String, val carried: LoopRound? = null, val viaCall: Boolean = false, val via: Via? = null)

/**
 * A value came out of the call of [callee] ([key]: [Escapes.key] of its overload) through its parameter [param], and before that
 * through [next] (null: [param] was given the memory itself). See [Escapes.resultRoots].
 */
data class Via(val key: String, val callee: String, val param: String, val next: Via?) {
    /** The calls, outermost first: `` `outer(...)`, given the result of `id(...)` ``. */
    fun shown(): String = generateSequence(this) { it.next }.joinToString(", given the result of ") { "`${it.callee}(...)`" }
}

/** `clearPool`/`destroy` of a pool at point [seq]: what was allocated in it before is freed. [how] says so in errors. */
data class Kill(val seq: Int, val how: String)

/** What is known about pools where a round of a loop ends: at the end of its body, at a `continue` or at a `break`. [vars] tells a variable from a later one of the same name. */
class RoundEnd(val vars: Map<String, Int>, val allocations: Map<String, Set<Alloc>>, val killed: Map<String, Kill>, val pools: Map<String, String>, val freed: Map<String, String>)

/** A use of [variable] (written [shown]) at [location] that may see its value from the previous round, after the pools in [killed] were freed in this round. */
class CarriedRead(val location: Loc, val shown: String, val variable: String, val killed: Map<String, Kill>)

/** A use at [location] of the pool variable [variable] (written [shown]), which held [pool] there. */
class PoolRead(val location: Loc, val shown: String, val variable: String, val pool: String)

/**
 * A loop being compiled (from point [head] of the function), which is compiled once, in source order. Every variable
 * holds an [Alloc] with this as `carried` when the loop starts, until it is assigned: a use that still sees it may see
 * the value of the previous round, so it is noted here, and checked against the [ends] of the rounds when the loop is compiled.
 */
class LoopRound(val head: Int) {
    var open = true
    val reads = mutableListOf<CarriedRead>()
    val poolReads = mutableListOf<PoolRead>()
    val ends = mutableListOf<RoundEnd>()
    /** Where a `break` leaves the loop, after what it runs: the code after the loop may see what is known there. */
    val breaks = mutableListOf<RoundEnd>()
}

/** A step of [Lifetimes] made in a loop, run again when the loop has been compiled, in the scopes it was made in. */
class Replay(val scopes: List<ContextStack.Scope>, val poolBlocks: List<Local>, val pipes: List<ITok>, val run: () -> Unit)

open class ContextStack {
    /**
     * One block scope. A child scope starts with copies of its parent's variables, deferred statements and
     * narrowed types, so narrowing inside an `if` body never leaks out of it.
     */
    class Scope(
        val vars: MutableMap<String, Int> = mutableMapOf(),
        val deferred: MutableList<ITok> = mutableListOf(),
        val narrowed: MutableMap<String, Type> = mutableMapOf(),
        /** Variable → the named-pool memory its value may be in (see [Lifetimes.allocations]). */
        val allocations: MutableMap<String, Set<Alloc>> = mutableMapOf(),
        /** Pool variable or field (`h.p`) → the pool it holds now (a new one after `p = new Pool`, the same as p after `val q = p`). */
        val pools: MutableMap<String, String> = mutableMapOf(),
        /** Pool → the last `clearPool`/`destroy` of it. */
        val killed: MutableMap<String, Kill> = mutableMapOf(),
        /** Pools freed with `destroy`, with how ("`destroy p` at line 9"). */
        val freed: MutableMap<String, String> = mutableMapOf(),
    ) {
        /** True once this scope always leaves early (ret, throw, break, continue); later code is unreachable. */
        var returned: Boolean = false
        /** True when [returned] only because a branch was picked at compile time (code after it is skipped, not an error). */
        var returnedStatically: Boolean = false
        val newDeferred: MutableList<ITok> = mutableListOf()
    }

    val vars: MutableList<IVar> = mutableListOf()
    val scopeStack: MutableList<Scope> = mutableListOf(Scope())

    /** The local memory each variable (by index in [vars]) may point into; see [Lifetimes]. */
    private val realOrigins = HashMap<Int, Local>()

    /**
     * The same while [imagining]: what each variable would point into if the loops of [LoopPools] gave each round a pool,
     * so it also holds the memory of those pools. Outside that, changes go to both maps, as the two agree there.
     */
    private val imaginedOrigins = HashMap<Int, Local>()

    /** Whether [Lifetimes] is following memory as if loops had pools of their own (see [LoopPools]). */
    var imagining = false

    private val origins get() = if (imagining) imaginedOrigins else realOrigins

    private fun setOriginAt(index: Int, local: Local?) {
        for (map in if (imagining) listOf(imaginedOrigins) else listOf(realOrigins, imaginedOrigins)) {
            val merged = if (local == null) null else Lifetimes.deepest(listOf(map[index], local))
            if (merged != null) map[index] = merged
        }
    }

    /** The local variables each variable (by index in [vars]) may point at, so writes through it change them. */
    private val pointees = HashMap<Int, Set<String>>()

    /** Pool → the pools it may be a child of: freeing a pool frees its children. */
    private val poolParents = HashMap<String, Set<String>>()

    /** The pools of the `pool do` blocks of this function, which only their block may free. */
    val blockPools = HashSet<String>()

    /** The pools this function made with `new Pool`, which it may clear and destroy without `unsafe`. */
    val ownedPools = HashSet<String>()

    /** The loops around the code being compiled, innermost last. */
    val rounds = mutableListOf<LoopRound>()

    /** What to run again when the outermost loop has been compiled (see [Lifetimes]). */
    val replays = mutableListOf<Replay>()

    /** How deep in `debug` statements the code being compiled is (see TDebug). */
    var debugDepth = 0

    /** The names defined in `debug` statements, with where, for the error when code after one uses them. */
    val debugDefined = HashMap<String, Loc>()

    /** The variables a `debug assert` (or `debug requires`) narrowed, with where, for the error when code after it relies on that. */
    val debugNarrowed = HashMap<String, Loc>()

    fun define(location: Loc, variable: IVar): IVar {
        if (debugDepth > 0) debugDefined.putIfAbsent(variable.getName(), location)
        val top = scopeStack.last()
        top.vars[variable.getName()]?.let { index ->
            val name = variable.getName()
            val outer = scopeStack.size > 1 && scopeStack[scopeStack.size - 2].vars[name] == index
            val where = if (outer) "in an enclosing block" else "in this block"
            val why = if (outer) " eclang doesn't let a nested block hide (shadow) a name, so a name means the same thing everywhere in a function." else ""
            throw EcError(location, "'$name' is already defined $where, as ${vars[index].kind()}.$why Choose a different name, e.g. one that says what this value is; or, to change the existing one, assign it (`$name = ...`, which needs a `var`).")
        }
        top.vars[variable.getName()] = vars.size
        top.narrowed.remove(variable.getName())
        vars.add(variable)
        return variable
    }

    fun defer(location: Loc, tok: ITok) {
        val top = scopeStack.last()
        top.deferred.add(tok)
        top.newDeferred.add(tok)
    }

    fun getIndexRaw(name: String): Int? = scopeStack.last().vars[name]

    fun getIndex(location: Loc, name: String): Int = getIndexRaw(name) ?: throw EcNotDefinedError(location, name)

    /** The variable called [name], with its narrowed type if flow analysis narrowed it in this scope. */
    fun getRaw(name: String): IVar? {
        val top = scopeStack.last()
        val variable = top.vars[name]?.let { vars[it] } ?: return null
        return top.narrowed[name]?.let { NarrowedVar(variable, it) } ?: variable
    }

    fun get(location: Loc, name: String): IVar = getRaw(name) ?: throw EcNotDefinedError(location, name)

    /** The depth of the scope that defined the local [name] (it is freed when that scope ends), or null if it isn't local. */
    fun definedDepth(name: String): Int? {
        val index = scopeStack.last().vars[name] ?: return null
        return scopeStack.indexOfFirst { it.vars[name] == index } + 1
    }

    /** The local memory the variable [name] may point into, or null. */
    fun originOf(name: String): Local? = scopeStack.last().vars[name]?.let { origins[it] }

    /** Notes that [name] may point into [local] too (see [Lifetimes.deepest]). */
    fun setOrigin(name: String, local: Local) {
        val index = scopeStack.last().vars[name] ?: return
        setOriginAt(index, local)
    }

    /** The variable [name] was just defined: it points into [local], and nothing it held in the previous round of a loop. */
    fun resetOrigin(name: String, local: Local?) {
        val index = scopeStack.last().vars[name] ?: return
        for (map in if (imagining) listOf(imaginedOrigins) else listOf(realOrigins, imaginedOrigins))
            if (local != null) map[index] = local else map.remove(index)
    }

    /** The local variables that [name] may point at. */
    fun pointeesOf(name: String): Set<String> = scopeStack.last().vars[name]?.let { pointees[it] } ?: setOf()

    /** Notes that [name] may point at [variables] too. */
    fun addPointees(name: String, variables: Set<String>) {
        val index = scopeStack.last().vars[name] ?: return
        if (variables.isNotEmpty()) pointees[index] = pointees[index].orEmpty() + variables
    }

    /** A value that changes when what [origins] and [pointees] know changes, to tell when replaying a loop has settled. */
    fun followed(): Any = listOf(realOrigins, imaginedOrigins).map { map -> map.mapValues { (_, local) -> generateSequence(local) { it.inside }.map { it.depth to it.loopPools.size }.toList() } } to pointees.toMap()

    /** Runs [run] as if [scopes] were the open scopes. */
    fun <R> inScopes(scopes: List<Scope>, run: () -> R): R {
        val saved = scopeStack.toList()
        scopeStack.clear(); scopeStack += scopes
        try { return run() } finally { scopeStack.clear(); scopeStack += saved }
    }

    /** From here to the end of the current scope, [name] is known to hold a [type]. */
    fun narrow(name: String, type: Type) { scopeStack.last().narrowed[name] = type }

    /**
     * The narrowed type under [key], a variable's name or `@p` for what the `mut` parameter p points to (the caller's variable,
     * which the code reads as `p`; see `TReadPointer.mutParameter`), or null when it isn't narrowed.
     */
    fun narrowedType(key: String): Type? = scopeStack.last().narrowed[key]

    /**
     * Forgets what is known about [name] (it was just assigned), in every enclosing scope too: after
     * `if v is Int do (if c do v = "s"c)` the outer body can no longer rely on v being an Int.
     */
    fun forget(name: String) {
        val variable = name.removePrefix("@")
        val index = scopeStack.last().vars[variable] ?: return
        scopeStack.forEach { if (it.vars[variable] == index) it.narrowed.remove(name) }
    }

    /** How many scopes are open; [deferredSince] takes such a depth. */
    fun depth(): Int = scopeStack.size

    /** The statements deferred in the scopes opened after [depth] was taken, in the order they must run (last deferred first). */
    fun deferredSince(depth: Int): List<ITok> = scopeStack.drop(depth).flatMap { it.newDeferred }.reversed()

    fun push() {
        val top = scopeStack.last()
        scopeStack.add(Scope(top.vars.toMutableMap(), top.deferred.toMutableList(), top.narrowed.toMutableMap(),
            top.allocations.toMutableMap(), top.pools.toMutableMap(), top.killed.toMutableMap(), top.freed.toMutableMap()))
        pendingRound?.takeIf { it.depth == depth() }?.let { round ->
            pendingRound = null
            defer(round.location, round.leave)
            round.registered()
        }
    }

    /**
     * A loop body about to be compiled whose rounds may get a pool of their own (see LoopPools): when the scope at [depth] (the
     * body's) is pushed, [leave] is deferred there, so that every way out of a round frees the round's pool, and [registered] runs.
     */
    class PendingRound(val depth: Int, val location: Loc, val leave: ITok, val registered: () -> Unit)

    var pendingRound: PendingRound? = null

    /**
     * Closes the innermost scope, whose code may or may not have run (a loop's body, a branch). Unless it always left
     * early, what it learned about pools carries on in the enclosing one, joined with what that knew: the memory it freed
     * may be freed, and a variable may hold what it held before or what the scope assigned. (A scope that left early,
     * e.g. by `ret` after running `defer destroy p`, changes nothing.) With [ran], the scope's code always ran when the
     * enclosing one got there (a block's own scope, see TBlock): what it knew replaces what the enclosing scope knew.
     */
    fun pop(ran: Boolean = false): Scope {
        val scope = scopeStack.removeLastOrNull() ?: throw Error("Scope empty")
        val parent = scopeStack.lastOrNull() ?: return scope
        if (scope.returned) return scope
        join(parent, listOf(snapshot(scope)), withInto = !ran)
        return scope
    }

    /** Closes the innermost scope, a branch of an `if`, without passing on what it knows: [joinBranches] does. Null when it always left early. */
    fun popBranch(): RoundEnd? {
        val scope = scopeStack.removeLastOrNull() ?: throw Error("Scope empty")
        return if (scope.returned) null else snapshot(scope)
    }

    /** After the branches of an `if` (see TStatementIf): what is known is what one of [ends], the ways on from it, knew. */
    fun joinBranches(ends: List<RoundEnd>) = join(scopeStack.last(), ends, withInto = false)

    /**
     * What one of the [ends] (and, [withInto], [into] itself) knows carries on in [into], as far as it is about what [into]
     * can see. Memory freed on one path may be freed. Following the paths, a variable may hold what it held on any of them,
     * and that is freed when it is freed on one of the paths that hold it: after `if c do (clearPool(p); s = format(p, ...))`,
     * the s of the other path holds what it held before, which that path didn't free. As [Kill]s are compared by when they
     * happened, memory that no path holding it freed is moved to now, after every kill so far (later ones still free it).
     */
    private fun join(into: Scope, ends: List<RoundEnd>, withInto: Boolean) {
        val paths = (if (withInto) listOf(snapshot(into)) else listOf()) + ends
        if (paths.isEmpty()) return
        // A name that isn't local in either is a global, which outlives both.
        fun visible(path: RoundEnd, name: String) = into.vars[name.substringBefore('.')] == path.vars[name.substringBefore('.')]
        fun dead(path: RoundEnd, a: Alloc) = a.carried == null && path.killed[a.pool].let { it != null && it.seq > a.seq }

        val killed = HashMap<String, Kill>()
        paths.forEach { p -> p.killed.forEach { (pool, kill) -> killed.merge(pool, kill) { a, b -> if (b.seq > a.seq) b else a } } }
        val now = next()
        val allocations = HashMap<String, Set<Alloc>>()
        val freedOnPath = HashSet<Alloc>()
        val held = HashMap<String, MutableSet<Alloc>>()
        for (p in paths) for ((name, allocs) in p.allocations) if (visible(p, name)) {
            held.getOrPut(name) { HashSet() } += allocs
            allocs.filter { dead(p, it) }.forEach { freedOnPath += it }
        }
        for ((name, allocs) in held) allocations[name] = allocs.map { a ->
            val seemsFreed = a.carried == null && killed[a.pool].let { it != null && it.seq > a.seq }
            if (seemsFreed && a !in freedOnPath) a.copy(seq = now) else a
        }.toSet()

        if (!withInto) {
            into.killed.clear()
            into.allocations.keys.removeAll { name -> paths.any { visible(it, name) } }
        }
        into.killed.putAll(killed)
        ends.forEach { into.freed.putAll(it.freed) }
        into.allocations.putAll(allocations)
        ends.forEach { end ->
            end.pools.forEach { (name, pool) -> if (visible(end, name)) into.pools[name] = pool }
        }
    }

    private fun snapshot(s: Scope) = RoundEnd(s.vars.toMap(), s.allocations.toMap(), s.killed.toMap(), s.pools.toMap(), s.freed.toMap())

    /** The code after a loop can be reached from each `break` in it ([ends]) too. */
    fun mergeBreaks(ends: List<RoundEnd>) = join(scopeStack.last(), ends, withInto = true)

    /** Runs [run], the deferred statements of a way out of this scope: what they free stays freed on that way out only. Also says whether they free anything. */
    fun <R> onExit(run: () -> R): Pair<R, Boolean> {
        push()
        val top = scopeStack.last()
        val before = top.killed.toMap()
        try {
            return run() to (top.killed != before)
        } finally {
            top.returned = true
            pop()
        }
    }

    /** A loop starts here: what variables hold from here on may also be what they held when a round ended. [holdsPointer]: whether a variable can hold memory. */
    fun openRound(holdsPointer: (String) -> Boolean) {
        val top = scopeStack.last()
        val round = LoopRound(next())
        rounds += round
        (top.vars.keys + top.allocations.keys).filter(holdsPointer).forEach { name ->
            top.allocations[name] = top.allocations[name].orEmpty() + Alloc(round.head, "", name, round)
        }
    }

    /** What is known about pools at this point, for a round of a loop that ends here. */
    fun roundEnd(): RoundEnd = snapshot(scopeStack.last())

    /** Pools freed since the start of [round]. */
    fun killedSince(round: LoopRound): Map<String, Kill> = scopeStack.last().killed.filterValues { it.seq > round.head }

    private var counter = 0

    /** The next point in the function, for [Alloc.seq] and [Kill.seq] (also names new pools). */
    fun next(): Int = counter++

    /** The named-pool memory the local [name] may point into. */
    fun allocationsOf(name: String): Set<Alloc> = scopeStack.last().allocations[name] ?: setOf()

    /** [name] (a local or a global) now holds a value in [allocs] (replacing what it held), or, with [add], also in them (a field of it was set). */
    fun setAllocations(name: String, allocs: Set<Alloc>, add: Boolean = false) {
        val top = scopeStack.last()
        if (add) top.allocations.merge(name, allocs, Set<Alloc>::plus) else top.allocations[name] = allocs
    }

    /** The place [name] now holds a value in [allocs], and so do the fields of it (`x.a` and `x.a.b` after `x.a = v`), which were replaced with it. */
    fun replaceAllocations(name: String, allocs: Set<Alloc>) {
        val top = scopeStack.last()
        top.allocations.keys.removeAll { it.startsWith("$name.") }
        top.allocations[name] = allocs
    }

    /** The pool that [name], a pool variable, global or field (`h.p`), holds. */
    fun poolOf(name: String): String = scopeStack.last().let { top -> top.pools.getOrPut(name) { poolId(top, name) } }

    /** [name] (see [poolOf]) now holds [pool] (`val q = p`), or a new pool when [pool] is null. */
    fun setPool(name: String, pool: String?) {
        val top = scopeStack.last()
        top.pools[name] = pool ?: "${poolId(top, name)}#${next()}"
    }

    private fun poolId(top: Scope, name: String): String {
        val root = name.substringBefore('.')
        return "pool${top.vars[root] ?: "@$root"}${name.drop(root.length)}"
    }

    /** [pool] is freed with [parents] at the latest. */
    fun setPoolParents(pool: String, parents: Set<String>) { poolParents[pool] = parents }

    /** The pools that freeing [pool] frees too. */
    private fun childrenOf(pool: String): Set<String> {
        val children = HashSet<String>()
        var added = true
        while (added) added = poolParents.filter { (child, parents) -> child !in children && parents.any { it == pool || it in children } }.keys.let(children::addAll)
        return children
    }

    /** The pools that freeing [pool] frees, itself included. */
    fun freedWith(pool: String): Set<String> = childrenOf(pool) + pool

    /** Memory allocated in [pool] (called [poolName]) now. */
    fun alloc(pool: String, poolName: String) = Alloc(next(), pool, poolName)

    /** Frees what [pool] holds (`clearPool`), or with [free] the pool itself (`destroy`); [how] says so in errors. */
    fun kill(pool: String, how: String, free: Boolean) {
        val top = scopeStack.last()
        val kill = Kill(next(), how)
        top.killed[pool] = kill
        if (free) top.freed[pool] = how
        // Clearing a pool also frees the pools made in it.
        childrenOf(pool).forEach { child ->
            top.killed[child] = kill
            top.freed[child] = how
        }
    }

    /** How the memory of [alloc] was freed, or null when it is still there. */
    fun killedBy(alloc: Alloc): String? = scopeStack.last().killed[alloc.pool]?.takeIf { it.seq > alloc.seq }?.how

    /** How [pool] was freed with `destroy`, or null. */
    fun freedBy(pool: String): String? = scopeStack.last().freed[pool]

    fun setReturned(statically: Boolean = false) {
        val top = scopeStack.last()
        if (!top.returned) top.returnedStatically = statically
        top.returned = true
    }

    fun hasReturnedStatically(): Boolean = scopeStack.last().returnedStatically

    fun hasReturned(): Boolean = scopeStack.last().returned

    fun getVars() = vars.toTypedArray()

    fun getDeferred(): MutableList<ITok> = scopeStack.last().deferred

    fun getNewDeferred(): MutableList<ITok> = scopeStack.last().newDeferred
}
