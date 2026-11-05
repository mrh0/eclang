package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.ect2.Type

/**
 * Memory allocated in [pool] ([poolName] in code) at point [seq] of the function, which `clearPool`/`freePool` of it later frees.
 * With [carried] it stands for "what the variable [poolName] held when the previous round of that loop ended" instead: see [LoopRound].
 * [viaCall]: a value only may be in it, because it came from a call given something that is.
 */
data class Alloc(val seq: Int, val pool: String, val poolName: String, val carried: LoopRound? = null, val viaCall: Boolean = false)

/** `clearPool`/`freePool` of a pool at point [seq]: what was allocated in it before is freed. [how] says so in errors. */
data class Kill(val seq: Int, val how: String)

/** What is known about pools where a round of a loop ends: at the end of its body, or at a `continue`. */
class RoundEnd(val allocations: Map<String, Set<Alloc>>, val killed: Map<String, Kill>, val pools: Map<String, String>, val freed: Map<String, String>)

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
        /** Pool variable or field (`h.p`) → the pool it holds now (a new one after `p = newPool()`, the same as p after `val q = p`). */
        val pools: MutableMap<String, String> = mutableMapOf(),
        /** Pool → the last `clearPool`/`freePool` of it. */
        val killed: MutableMap<String, Kill> = mutableMapOf(),
        /** Pools freed with `freePool`, with how ("`freePool(p)` at line 9"). */
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
    private val origins = HashMap<Int, Local>()

    /** The local variables each variable (by index in [vars]) may point at, so writes through it change them. */
    private val pointees = HashMap<Int, Set<String>>()

    /** Pool → the pools it may be a child of: freeing a pool frees its children. */
    private val poolParents = HashMap<String, Set<String>>()

    /** The pools of the `pool do` blocks of this function, which only their block may free. */
    val blockPools = HashSet<String>()

    /** The loops around the code being compiled, innermost last. */
    val rounds = mutableListOf<LoopRound>()

    /** What to run again when the outermost loop has been compiled (see [Lifetimes]). */
    val replays = mutableListOf<Replay>()

    fun define(location: Loc, variable: IVar): IVar {
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
        Lifetimes.deepest(listOf(origins[index], local))?.let { origins[index] = it }
    }

    /** The variable [name] was just defined: it points into [local], and nothing it held in the previous round of a loop. */
    fun resetOrigin(name: String, local: Local?) {
        val index = scopeStack.last().vars[name] ?: return
        if (local != null) origins[index] = local else origins.remove(index)
    }

    /** The local variables that [name] may point at. */
    fun pointeesOf(name: String): Set<String> = scopeStack.last().vars[name]?.let { pointees[it] } ?: setOf()

    /** Notes that [name] may point at [variables] too. */
    fun addPointees(name: String, variables: Set<String>) {
        val index = scopeStack.last().vars[name] ?: return
        if (variables.isNotEmpty()) pointees[index] = pointees[index].orEmpty() + variables
    }

    /** A value that changes when what [origins] and [pointees] know changes, to tell when replaying a loop has settled. */
    fun followed(): Any = origins.mapValues { (_, local) -> generateSequence(local) { it.inside }.map { it.depth }.toList() } to pointees.toMap()

    /** Runs [run] as if [scopes] were the open scopes. */
    fun <R> inScopes(scopes: List<Scope>, run: () -> R): R {
        val saved = scopeStack.toList()
        scopeStack.clear(); scopeStack += scopes
        try { return run() } finally { scopeStack.clear(); scopeStack += saved }
    }

    /** From here to the end of the current scope, [name] is known to hold a [type]. */
    fun narrow(name: String, type: Type) { scopeStack.last().narrowed[name] = type }

    /**
     * Forgets what is known about [name] (it was just assigned), in every enclosing scope too: after
     * `if v is Int do (if c do v = "s"c)` the outer body can no longer rely on v being an Int.
     */
    fun forget(name: String) {
        val index = scopeStack.last().vars[name] ?: return
        scopeStack.forEach { if (it.vars[name] == index) it.narrowed.remove(name) }
    }

    /** How many scopes are open; [deferredSince] takes such a depth. */
    fun depth(): Int = scopeStack.size

    /** The statements deferred in the scopes opened after [depth] was taken, in the order they must run (last deferred first). */
    fun deferredSince(depth: Int): List<ITok> = scopeStack.drop(depth).flatMap { it.newDeferred }.reversed()

    fun push() {
        val top = scopeStack.last()
        scopeStack.add(Scope(top.vars.toMutableMap(), top.deferred.toMutableList(), top.narrowed.toMutableMap(),
            top.allocations.toMutableMap(), top.pools.toMutableMap(), top.killed.toMutableMap(), top.freed.toMutableMap()))
    }

    /**
     * Closes the innermost scope. Unless it always left early, what it learned about pools carries on in the
     * enclosing one: the memory it freed stays freed, and a variable may hold what it held before or what the
     * scope assigned. (A scope that left early, e.g. by `ret` after running `defer freePool(p)`, changes nothing.)
     */
    fun pop(): Scope {
        val scope = scopeStack.removeLastOrNull() ?: throw Error("Scope empty")
        val parent = scopeStack.lastOrNull() ?: return scope
        if (scope.returned) return scope
        scope.killed.forEach { (pool, kill) -> parent.killed.merge(pool, kill) { a, b -> if (b.seq > a.seq) b else a } }
        parent.freed.putAll(scope.freed)
        // A name that isn't local in either is a global, which outlives both.
        scope.allocations.forEach { (name, allocs) ->
            if (parent.vars[name] == scope.vars[name]) parent.allocations.merge(name, allocs, Set<Alloc>::plus)
        }
        scope.pools.forEach { (name, pool) ->
            val root = name.substringBefore('.')
            if (parent.vars[root] == scope.vars[root]) parent.pools[name] = pool
        }
        return scope
    }

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
    fun roundEnd(): RoundEnd = scopeStack.last().let { RoundEnd(it.allocations.toMap(), it.killed.toMap(), it.pools.toMap(), it.freed.toMap()) }

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

    /** Frees what [pool] holds (`clearPool`), or with [free] the pool itself (`freePool`); [how] says so in errors. */
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

    /** How [pool] was freed with `freePool`, or null. */
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
