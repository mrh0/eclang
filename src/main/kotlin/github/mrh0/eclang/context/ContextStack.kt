package github.mrh0.eclang.context

import github.mrh0.eclang.ast.ITok
import github.mrh0.eclang.ast.Loc
import github.mrh0.eclang.context.state.IVar
import github.mrh0.eclang.context.state.NarrowedVar
import github.mrh0.eclang.error.EcError
import github.mrh0.eclang.error.EcNotDefinedError
import github.mrh0.ect2.Type

/** Memory allocated in [pool] ([poolName] in code) at point [seq] of the function, which `clearPool`/`freePool` of it later frees. */
data class Alloc(val seq: Int, val pool: String, val poolName: String)

/** `clearPool`/`freePool` of a pool at point [seq]: what was allocated in it before is freed. [how] says so in errors. */
data class Kill(val seq: Int, val how: String)

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
        /** Pool variable → the pool it holds now (a new one after `p = newPool()`, the same as p after `val q = p`). */
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

    /** Notes that [name] may point into [local] (keeping the memory freed first when it already may point elsewhere). */
    fun setOrigin(name: String, local: Local) {
        val index = scopeStack.last().vars[name] ?: return
        val old = origins[index]
        if (old == null || local.depth > old.depth) origins[index] = local
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
        scope.allocations.forEach { (name, allocs) ->
            if (parent.vars[name] != null && parent.vars[name] == scope.vars[name]) parent.allocations.merge(name, allocs, Set<Alloc>::plus)
        }
        scope.pools.forEach { (name, pool) -> if (parent.vars[name] != null && parent.pools[name] != pool) parent.pools[name] = pool }
        return scope
    }

    private var counter = 0

    /** The next point in the function, for [Alloc.seq] and [Kill.seq] (also names new pools). */
    fun next(): Int = counter++

    /** The named-pool memory the local [name] may point into. */
    fun allocationsOf(name: String): Set<Alloc> = scopeStack.last().allocations[name] ?: setOf()

    /** [name] now holds a value in [allocs] (replacing what it held), or, with [add], also in them (a field of it was set). */
    fun setAllocations(name: String, allocs: Set<Alloc>, add: Boolean = false) {
        val top = scopeStack.last()
        if (top.vars[name] == null) return
        if (add) top.allocations.merge(name, allocs, Set<Alloc>::plus) else top.allocations[name] = allocs
    }

    /** The pool the pool variable [name] holds, or null when it isn't a local. */
    fun poolOf(name: String): String? {
        val top = scopeStack.last()
        val index = top.vars[name] ?: return null
        return top.pools.getOrPut(name) { "pool$index" }
    }

    /** The pool variable [name] now holds [pool] (`val q = p`), or a new pool when [pool] is null. */
    fun setPool(name: String, pool: String?) {
        val top = scopeStack.last()
        val index = top.vars[name] ?: return
        top.pools[name] = pool ?: "pool$index#${next()}"
    }

    /** Memory allocated in [pool] (called [poolName]) now. */
    fun alloc(pool: String, poolName: String) = Alloc(next(), pool, poolName)

    /** Frees what [pool] holds (`clearPool`), or with [free] the pool itself (`freePool`); [how] says so in errors. */
    fun kill(pool: String, how: String, free: Boolean) {
        val top = scopeStack.last()
        top.killed[pool] = Kill(next(), how)
        if (free) top.freed[pool] = how
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
